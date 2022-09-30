import functools
from flask import session, request, abort
from flask_login import current_user
from flask_talisman import talisman
from flask_wtf.csrf import CSRFProtect, generate_csrf
from redash.handlers.base import get_object_or_404
from redash import models
import logging
import requests
import os

from redash import settings


talisman = talisman.Talisman()
csrf = CSRFProtect()

logger = logging.getLogger("authentication")


def get_profile_info(bearerToken):
    # Get user profiles
    str_api = os.environ.get('DAEU_API_HOST')
    str_formio_base = os.environ.get('DAEU_FORMIO_BASE_PROJECT')
    url = str_api + '/projects/' + str_formio_base + '/user-profile'
    headers = {'Authorization': "Bearer " + bearerToken}
    logging.info(url)
    response = requests.get(url, headers=headers, verify=False)

    # Check if profile json is correct
    profile_json = None
    if response.status_code != 200:
        abort(403)

    try:
        profile_json = response.json()
    except requests.exceptions.JSONDecodeError:
        logging.error(
            'Failed loading profile info'.format(
                url=url,
            )
        )
        abort(403)

    return profile_json


def get_supplier_info(applicant, bearerToken):
    # If applicant is set get supplier info
    str_api = os.environ.get('DAEU_API_HOST')
    str_formio_base = os.environ.get('DAEU_FORMIO_BASE_PROJECT')
    url = str_api + '/project/' + str_formio_base + '/common/nom/service-supplier/submission'
    payload = {'data.code': applicant}

    # Get supplier info
    headers = {'Authorization': "Bearer " + bearerToken}
    logging.info(url)
    response = requests.get(url, headers=headers, params=payload, verify=False)

    supplier_json = None
    if response.status_code != 200:
        abort(403)
    try:
        supplier_json = response.json()
    except requests.exceptions.JSONDecodeError:
        logging.error(
            'Failed loading JSON response from {url} for applicant: {applicant}'.format(
                url=url,
                applicant=applicant
            )
        )
        abort(403)
    return supplier_json


def is_eik_in_profiles(eik, profiles):
    # If any of the identifiers are equal to the eik
    # all is good continue
    for profile in profiles:
        logging.info('{identifier} == {eik}'.format(identifier=profile.get('identifier'), eik=eik))
        if profile.get('identifier') == eik:
            return True
    return False


def valid_supplier(fn):
    @functools.wraps(fn)
    def decorated(*args, **kwargs):
        # Get dashboard widgets
        obj_api_instance = get_object_or_404(models.ApiKey.get_by_api_key, request.view_args.get('token'))
        obj_object_instance = obj_api_instance.object
        obj_widget_list = obj_object_instance.widgets.all()

        applicant_ids = []

        # Go through the widgets to see if an applicant parameter is set
        applicant = None
        for widget in obj_widget_list:
            widget_parameters = widget.visualization.query_rel.options.get('parameters')
            for parameter in widget_parameters:
                name = parameter.get('name')
                if name == 'applicant':
                    applicant = parameter.get('value')

        # If no applicant is set, continue
        if applicant is None:
            return fn(*args, **kwargs)

        bearerToken = request.args.get('auth_token', None)
        if not bearerToken:
            logging.error('Trying to get applicant {applicant} without bearer token'.format(applicant=applicant))
            abort(403)

        supplier_json = get_supplier_info(applicant, bearerToken)

        # If supplier_json is not defined or an empty array
        # something went wrong abort
        if supplier_json is None or not supplier_json:
            abort(403)

        # Get eik from supplier info,
        # We use [0] because we expect only one element to be returned
        # if no eik is found 403
        supplier = supplier_json[0]
        eik = supplier.get('data', {}).get('eik')
        logging.info(eik)
        if eik is None:
            abort(403)

        # Get user profiles
        profile_json = get_profile_info(bearerToken)
        # Quit if return object has no profiles,
        # or returned array is empty
        profiles = profile_json.get('profiles')
        logging.info('profiles')
        if profiles is None or not profiles:
            abort(403)

        if is_eik_in_profiles(eik, profiles):
            return fn(*args, **kwargs)

        abort(403)

    return decorated

def csp_allows_embeding(fn):
    @functools.wraps(fn)
    def decorated(*args, **kwargs):
        return fn(*args, **kwargs)

    embedable_csp = talisman.content_security_policy + "frame-ancestors *;"
    return talisman(content_security_policy=embedable_csp, frame_options=None)(
        decorated
    )


def init_app(app):
    csrf.init_app(app)
    app.config["WTF_CSRF_CHECK_DEFAULT"] = False
    app.config["WTF_CSRF_SSL_STRICT"] = False
    app.config["WTF_CSRF_TIME_LIMIT"] = settings.CSRF_TIME_LIMIT

    @app.after_request
    def inject_csrf_token(response):
        response.set_cookie("csrf_token", generate_csrf())
        return response

    if settings.ENFORCE_CSRF:
        @app.before_request
        def check_csrf():
            # BEGIN workaround until https://github.com/lepture/flask-wtf/pull/419 is merged
            if request.blueprint in csrf._exempt_blueprints:
                return

            view = app.view_functions.get(request.endpoint)
            dest = f'{view.__module__}.{view.__name__}'

            if dest in csrf._exempt_views:
                return
            # END workaround

            if not current_user.is_authenticated or "user_id" in session:
                csrf.protect()

    talisman.init_app(
        app,
        feature_policy=settings.FEATURE_POLICY,
        force_https=settings.ENFORCE_HTTPS,
        force_https_permanent=settings.ENFORCE_HTTPS_PERMANENT,
        force_file_save=settings.ENFORCE_FILE_SAVE,
        frame_options=settings.FRAME_OPTIONS,
        frame_options_allow_from=settings.FRAME_OPTIONS_ALLOW_FROM,
        strict_transport_security=settings.HSTS_ENABLED,
        strict_transport_security_preload=settings.HSTS_PRELOAD,
        strict_transport_security_max_age=settings.HSTS_MAX_AGE,
        strict_transport_security_include_subdomains=settings.HSTS_INCLUDE_SUBDOMAINS,
        content_security_policy=settings.CONTENT_SECURITY_POLICY,
        content_security_policy_report_uri=settings.CONTENT_SECURITY_POLICY_REPORT_URI,
        content_security_policy_report_only=settings.CONTENT_SECURITY_POLICY_REPORT_ONLY,
        content_security_policy_nonce_in=settings.CONTENT_SECURITY_POLICY_NONCE_IN,
        referrer_policy=settings.REFERRER_POLICY,
        session_cookie_secure=settings.SESSION_COOKIE_SECURE,
        session_cookie_http_only=settings.SESSION_COOKIE_HTTPONLY,
    )
