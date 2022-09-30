import * as React from 'react';
import "../../i18n/i18n";
declare type DeployProps = {
    camundaApi: object;
};
declare type DeployState = {
    showModal: boolean;
};
export default class DeployButton extends React.Component<DeployProps, DeployState> {
    state: {
        showModal: boolean;
    };
    toggleModal: (e: any) => void;
    render(): JSX.Element;
}
export {};
