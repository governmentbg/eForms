/**
 * © Nowina Solutions, 2015-2015
 *
 * Concédée sous licence EUPL, version 1.1 ou – dès leur approbation par la Commission européenne - versions ultérieures de l’EUPL (la «Licence»).
 * Vous ne pouvez utiliser la présente œuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous la Licence est distribué «en l’état»,
 * SANS GARANTIES OU CONDITIONS QUELLES QU’ELLES SOIENT, expresses ou implicites.
 * Consultez la Licence pour les autorisations et les restrictions linguistiques spécifiques relevant de la Licence.
 */
package lu.nowina.nexu.flow.operation;

import java.util.Map;

import lu.nowina.nexu.InternalAPI;
import lu.nowina.nexu.api.DetectedCard;
import lu.nowina.nexu.api.Feedback;
import lu.nowina.nexu.api.FeedbackStatus;
import lu.nowina.nexu.api.NexuAPI;
import lu.nowina.nexu.api.ScAPI;
import lu.nowina.nexu.api.flow.BasicOperationStatus;
import lu.nowina.nexu.api.flow.OperationResult;
import lu.nowina.nexu.view.core.UIOperation;

/**
 * This {@link CompositeOperation} allows to provide some feedback in case of advanced creation.
 *
 * Expected parameters:
 * <ol>
 * <li>{@link NexuAPI}</li>
 * <li>{@link Map} whose keys are {@link TokenOperationResultKey} and values are {@link Object}.</li>
 * </ol>
 *
 * @author Jean Lepropre (jean.lepropre@nowina.lu)
 */
public class AdvancedCreationFeedbackOperation extends AbstractCompositeOperation<Void> {

    private NexuAPI api;
    private Map<TokenOperationResultKey, Object> map;

    public AdvancedCreationFeedbackOperation() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setParams(final Object... params) {
        try {
            this.api = (NexuAPI) params[0];
            this.map = (Map<TokenOperationResultKey, Object>) params[1];
        } catch(final ClassCastException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Expected parameters: NexuAPI, Map");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public OperationResult<Void> perform() {
        String apiParameter = (String) this.map.get(TokenOperationResultKey.SELECTED_API_PARAMS);
        ScAPI scApi = (ScAPI) this.map.get(TokenOperationResultKey.SELECTED_API);
        DetectedCard detectedCard = (DetectedCard) this.map.get(TokenOperationResultKey.SELECTED_PRODUCT);

        if (detectedCard != null && scApi != null &&
                (scApi == ScAPI.MOCCA || scApi == ScAPI.MSCAPI || apiParameter != null)) {
            ((InternalAPI) this.api).store(detectedCard.getAtr(), scApi, apiParameter);
        }
        return new OperationResult<Void>((Void) null);
    }

}
