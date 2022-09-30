import * as React from 'react';
import DeployModal from './DeployModal';

import { t } from 'i18next';
import "../../i18n/i18n";

type DeployProps = {
    camundaApi: object
}

type DeployState = {
    showModal: boolean,
}

export default class DeployButton extends React.Component<DeployProps, DeployState> {
    state = {
        showModal: false
    }

    toggleModal = e => {
        this.setState({
            showModal: !this.state.showModal
        })
    }

    render() {
        return (
            <>
                <a style={{cursor: "pointer"}} onClick={e => this.toggleModal(e)}>{t('DEPLOY')}</a>
                <DeployModal onClose={this.toggleModal} showModal={this.state.showModal} camundaApi={this.props.camundaApi}/>
            </>
        );
    }
}
