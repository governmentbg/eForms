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
(function() {
    window.addEventListener('load', (event) => {
         document.querySelector("#email")
            .addEventListener("keyup", function(e) {
                document.querySelector("#username").value = e.target.value;
            })
            .addEventListener("change", function(e) {
                document.querySelector("#username").value = e.target.value;
            });
    });
}())