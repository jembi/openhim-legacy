package org.jembi.rhea.orchestration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.store.ObjectStoreException;
import org.mule.routing.AbstractAggregator;
import org.mule.routing.AggregationException;
import org.mule.routing.EventGroup;
import org.mule.routing.correlation.CollectionCorrelatorCallback;
import org.mule.routing.correlation.EventCorrelatorCallback;

public class XDSRepositoryRetrieveDocumentSetResponseAggregator extends AbstractAggregator {

	@Override
	protected EventCorrelatorCallback getCorrelatorCallback(MuleContext muleContext) {
		return new CollectionCorrelatorCallback(muleContext, persistentStores, storePrefix){
			@SuppressWarnings("unchecked")
			@Override
			public MuleEvent aggregateEvents(EventGroup events) throws AggregationException {
				List<String> documentList = new ArrayList<String>();
				try {
					for (Iterator<MuleEvent> iterator = events.iterator(); iterator.hasNext();){
						MuleEvent event = iterator.next();
						documentList.addAll((List<String>)event.getMessage().getPayload());
					}
				} catch (ObjectStoreException ex) {
					throw new AggregationException(events, null, ex);
				}
				return new DefaultMuleEvent(new DefaultMuleMessage(documentList, muleContext), events.getMessageCollectionEvent());
			}
		};
	}

}
