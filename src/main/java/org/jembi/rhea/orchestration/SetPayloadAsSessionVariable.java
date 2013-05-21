package org.jembi.rhea.orchestration;

import org.jembi.rhea.Constants;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

/**
 * Adds a session variable to a message with the messages' payload
 *
 *	TODO The property name should be configurable (currently hardcoded for use by XDS retrieve documents)
 */
public class SetPayloadAsSessionVariable implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage msg = eventContext.getMessage();
		msg.setProperty(Constants.XDS_ITI_43, msg.getPayload(), PropertyScope.SESSION);
		return msg;
	}
}
