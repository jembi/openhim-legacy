package org.jembi.rhea.xds;

public class DocumentMetaData {
	
	private String homeCommunityId;
	private String documentUniqueId;
	
	public DocumentMetaData(String uniqueDocId, String homeCommunityId) {
		this.documentUniqueId = uniqueDocId;
		this.homeCommunityId = homeCommunityId;
	}
	public String getHomeCommunityId() {
		return homeCommunityId;
	}
	public void setHomeCommunityId(String homeCommunityId) {
		this.homeCommunityId = homeCommunityId;
	}
	public String getDocumentUniqueId() {
		return documentUniqueId;
	}
	public void setDocumentUniqueId(String documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}
	
}
