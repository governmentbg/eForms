/*******************************************************************************
 * Copyright (c) 2022 Digitall Nature Bulgaria
 *
 * This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Stefan Tabakov
 *    Nedka Taskova
 *    Stanimir Stoyanov
 *    Pavel Koev
 *    Igor Radomirov
 *******************************************************************************/
module.directive('citizenAttributesButton', function() {
    return {
        restrict: 'A',
        link: function(scope) {
            scope.addRequestedCitizenAttribute = function() {
                if (!Array.isArray(scope.selectedCitizenAttributes) ||
                    scope.selectedCitizenAttributes.length < 1) {
                    return;
                }
                scope.selectedCitizenAttributesToAdd = scope.selectedCitizenAttributes;
                
                var eauth2_citizenAttributesArray = [];
                if (scope.identityProvider.config.eauth2_citizenAttributes !== undefined &&
                    scope.identityProvider.config.eauth2_citizenAttributes.length > 0) {
                    eauth2_citizenAttributesArray = scope.identityProvider.config.eauth2_citizenAttributes.split(',');
                }
                var eauth2_requestedCitizenAttributesArray = [];
                if (scope.identityProvider.config.eauth2_requestedCitizenAttributes !== undefined && 
                    scope.identityProvider.config.eauth2_requestedCitizenAttributes.length > 0) {
                    eauth2_requestedCitizenAttributesArray = scope.identityProvider.config.eauth2_requestedCitizenAttributes.split(',');
                }        
                
                for (var i = 0; i < scope.selectedCitizenAttributesToAdd.length; i++) {
                    var selectedCitizenAttribute = scope.selectedCitizenAttributesToAdd[i];
                    var index = eauth2_citizenAttributesArray.indexOf(selectedCitizenAttribute);
                    if (index > -1) {
                        eauth2_citizenAttributesArray.splice(index, 1);
                        eauth2_requestedCitizenAttributesArray.push(selectedCitizenAttribute);
                    }
                }
                scope.identityProvider.config.eauth2_citizenAttributes = eauth2_citizenAttributesArray.join(',');
                scope.identityProvider.config.eauth2_requestedCitizenAttributes = eauth2_requestedCitizenAttributesArray.join(',');
                scope.selectedCitizenAttributes = [];
                scope.selectedCitizenAttributesToAdd = [];
            }
            scope.deleteRequestedCitizenAttribute = function() {
                if (!Array.isArray(scope.selectedRequestedCitizenAttributes) ||
                    scope.selectedRequestedCitizenAttributes.length < 1) {
                    return;
                }

                scope.selectedRequestedCitizenAttributesToRemove = scope.selectedRequestedCitizenAttributes;
                
                var eauth2_requestedCitizenAttributesArray = [];
                if (scope.identityProvider.config.eauth2_requestedCitizenAttributes !== undefined &&
                    scope.identityProvider.config.eauth2_requestedCitizenAttributes.length > 0) {
                    eauth2_requestedCitizenAttributesArray = scope.identityProvider.config.eauth2_requestedCitizenAttributes.split(',');    
                }
                var eauth2_citizenAttributesArray = [];
                if (scope.identityProvider.config.eauth2_citizenAttributes !== undefined &&
                    scope.identityProvider.config.eauth2_citizenAttributes.length > 0) {
                    eauth2_citizenAttributesArray = scope.identityProvider.config.eauth2_citizenAttributes.split(',');
                }
        
                for (var i = 0; i < scope.selectedRequestedCitizenAttributesToRemove.length; i++) {
                    var selectedCitizenAttribute = scope.selectedRequestedCitizenAttributesToRemove[i];
                    var index = eauth2_requestedCitizenAttributesArray.indexOf(selectedCitizenAttribute);
                    if (index > -1) {
                        eauth2_requestedCitizenAttributesArray.splice(index, 1);
                        eauth2_citizenAttributesArray.push(selectedCitizenAttribute);
                    }
                }
                scope.identityProvider.config.eauth2_citizenAttributes = eauth2_citizenAttributesArray.join(',');
                scope.identityProvider.config.eauth2_requestedCitizenAttributes = eauth2_requestedCitizenAttributesArray.join(',');
                scope.selectedRequestedCitizenAttributes = [];
                scope.selectedRequestedCitizenAttributesToRemove = [];
            }
        },
    };
});