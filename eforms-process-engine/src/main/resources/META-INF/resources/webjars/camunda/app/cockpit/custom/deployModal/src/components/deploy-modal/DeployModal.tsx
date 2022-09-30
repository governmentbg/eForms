import React from 'react';
import {
    Modal,
    Button,
    Form,
    FormFile,
    Toast
    } from 'react-bootstrap';
import Feedback from 'react-bootstrap/Feedback';

import { t } from 'i18next';
import "../../i18n/i18n";

type ModalProps = {
    onClose: Function,
    showModal: boolean,
    camundaApi: any
}

type FormState = {
    showToast: boolean
}
export default class DeployModal extends React.Component<ModalProps, FormState> {
    fileRef;
    toastMessage;
    constructor(props) {
        super(props);
        this.submit = this.submit.bind(this);
        this.onClose = this.onClose.bind(this);
        this.fileRef = React.createRef();
    }

    state = {
        showToast: false
    }
    onClose = e => {
        this.props.onClose && this.props.onClose(e);
        this.setState({showToast: false});
      };

    showToast = () => {
        this.setState({showToast: true});
    }
    submit = event => {
        event.preventDefault();
        event.stopPropagation();
        if (this.fileRef.current.files.length < 1) {
            this.fileRef.current.setAttribute("isInvalid", true);
            return;
        }

        if(!this.fileRef.current.files[0].name.includes('.bpmn')) {
            this.fileRef.current.setAttribute("isInvalid", true);
            return;
        }

        let formData = new FormData();
        formData.append('*', this.fileRef.current.files[0]);
        formData.set('deployment-source', 'Camunda Cockpit');
        formData.set('deployment-name', this.fileRef.current.files[0].name.replace('.bpmn', ''));

        fetch('/engine-rest/deployment/create', {
            method: "post",
            headers: {
                "Accept": "application/json",
                "X-XSRF-TOKEN": this.props.camundaApi.CSRFToken,
            },
            body: formData,
        })
        .then((response) => {
            if (response.status === 200) {
                this.toastMessage = t('DEPLOYMENT_SUCCESSFUl')
            } else {
                this.toastMessage = t('DEPLOY_ERROR', {ERROR: response.statusText});
            }
            this.showToast();
        })
        .catch(console.error)
    }

    render() {
        return (
            <>
                <Modal show={this.props.showModal} onHide={this.onClose} animation={false}>
                    <Modal.Header>
                        <Modal.Title>{t('DEPLOY_DIAGRAM')}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Toast animation={false} show={this.state.showToast}>
                            <Toast.Body>{this.toastMessage}</Toast.Body>
                        </Toast>
                        <Form noValidate onSubmit={e => this.submit(e)}>
                            <Form.Group controlId="form.file">
                                <FormFile>
                                    <FormFile.Label>{t('PROCESS_MODEL')}</FormFile.Label>
                                    <FormFile.Input
                                        required
                                        ref={this.fileRef}
                                    />
                                    <Feedback type="invalid">{t('BPMN_REQUIRED')}</Feedback>
                                </FormFile>
                            </Form.Group>
                            <Form.Row className="pull-right">
                                <Button disabled={this.state.showToast} variant="primary" type="submit" >
                                    {t('SUBMIT')}
                                </Button>
                                <Button variant="secondary" onClick={this.onClose}>
                                    {t('CLOSE')}
                                </Button>
                            </Form.Row>
                        </Form>
                    </Modal.Body>
                </Modal>
            </>
        );
      }
}