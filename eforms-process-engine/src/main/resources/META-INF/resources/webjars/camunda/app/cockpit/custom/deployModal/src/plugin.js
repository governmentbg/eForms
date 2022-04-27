import React from 'react';
import ReactDOM from 'react-dom';

import DeployButton from './components/deploy-modal/DeployButton';
let container;

export default {
    // id for plugin
    id: "deploy-modal",
    // location where plugin goes
    pluginPoint: "cockpit.navigation",
    priority: 1000,
    // what to render, specific objects that you can pass into render function to use
    render: (node, {api}) => {
        // create the actual button with an image inside + hard-wired styling
        // onclick function for our button
        container = node;

        ReactDOM.render(
            <DeployButton camundaApi={api}/>,
            container
        );
    },
    unmount: () => {
    ReactDOM.unmountComponentAtNode(container);
    }
};