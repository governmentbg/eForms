import React from 'react';
import "../../i18n/i18n";
declare type ModalProps = {
    onClose: Function;
    showModal: boolean;
    camundaApi: any;
};
declare type FormState = {
    showToast: boolean;
};
export default class DeployModal extends React.Component<ModalProps, FormState> {
    fileRef: any;
    toastMessage: any;
    constructor(props: any);
    state: {
        showToast: boolean;
    };
    onClose: (e: any) => void;
    showToast: () => void;
    submit: (event: any) => void;
    render(): JSX.Element;
}
export {};
