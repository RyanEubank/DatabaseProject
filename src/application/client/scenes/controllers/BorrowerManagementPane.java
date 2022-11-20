package src.application.client.scenes.controllers;

public class BorrowerManagementPane extends AbstractPane {

	@Override
	public void postInitialize() {
		super.postInitialize();
	}

	@Override
	protected void onPerformAction() {
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * Returns the name of the action performed by the borrower
	 * management pane: 'Create' for adding a new borrower and
	 * issuing a library card.
	 */
	@Override
	protected String getActionName() {
		return "Create";
	}
}
