## eForm Camunda Cockpit plugin for deploying processes.

The plugin has been developed with `React 17` and `React Bootstrap 1.6.1 (Bootstrap 4)`

    The already built version of the plugin is located in dist/plugin.js
    This is the file that is imported in config.js

    In order to build a new version of the plugin
    Edit the code in the src directory
    then run rollup -c in this directory

    While editing you can use rollup -c -w 
    to enable watcher for automatic rebuild of the plugin.
    You will need to rebuild or restart the eForms Process Engine service
    in order to apply the updates.
