export class SignatureOptionsEnum {
    static readonly requestorSignature = { value:"requestorSignature", label: 'REQUESTOR_SIGNATURE', exposable: true};
    static readonly requestorAndSigneesSignature = { value:"requestorAndSigneesSignature", label: 'REQUESTOR_AND_SIGNEES_SIGNATURE', exposable: true};
    static readonly signeesSignature = { value:"signeesSignature", label: 'SIGNEES_SIGNATURE', exposable: true};

    /**
     * Returns an array like [{value: ..., label:..., <other optional properties>}...]
     * only for the exposable (true) objects of this class.
     *  
     */
    public static getSignatureOptionsArray(): any {
        let signatureOptionArray = Object.getOwnPropertyNames(SignatureOptionsEnum).map(ownPropertyName => {
            
            let signatureOptionElement = Object.assign({}, SignatureOptionsEnum[ownPropertyName])
            if(signatureOptionElement.exposable){
                delete signatureOptionElement.exposable
                return signatureOptionElement
            }
        }).filter( so => so !== undefined);

        return signatureOptionArray;
    }

    /**
     * Returns a model object like {this_class_property : {value: ..., label:..., <other optional properties>}...}
     * only for the exposable (true) objects of this class.
     *  
     */
    public static getSignatureOptionsObjectModel(): any {

        let objectModel = {}
        Object.getOwnPropertyNames(SignatureOptionsEnum).map(ownPropertyName => {
            
            let signatureOptionElement = Object.assign({}, SignatureOptionsEnum[ownPropertyName])
            if(signatureOptionElement.exposable){
                delete signatureOptionElement.exposable
                objectModel[ownPropertyName] = signatureOptionElement
            }
        });

        return objectModel;

    }
}