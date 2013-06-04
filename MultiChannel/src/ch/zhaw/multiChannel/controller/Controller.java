package ch.zhaw.multiChannel.controller;

import ch.zhaw.multiChannel.model.*;
import ch.zhaw.multiChannel.view.*;

public class Controller {
	
	private StartPage startPage;
	private Page openedPage;
	private PageType currentPageType;

	// Test
    public static void main(String[] args) {
		new Controller().Start();
    }

	private void Start() {
		startPage = new StartPage(this);
		startPage.Show();
	}

	public void ComposeSms() {
		currentPageType = PageType.Sms;
		openMessagePage("SMS");
	}

	public void ComposeFax() {
		currentPageType = PageType.Fax;
		openMessagePage("Fax");
	}

	public void ComposeMms() {
		currentPageType = PageType.Mms;
		openAttachmentMessagePage("MMS");
	}

	public void ComposeEmail() {
		currentPageType = PageType.Email;
		openAttachmentMessagePage("E-Mail");
	}

	private void openMessagePage(String title) {
		if (openedPage != null && openedPage.IsVisible()) {
			startPage.ShowOnlyOnePageMessage();
			return;
		}
		openedPage = new MessagePage(this);
		openedPage.Show(title);
	}

	private void openAttachmentMessagePage(String title) {
		if (openedPage != null && openedPage.IsVisible()) {
			startPage.ShowOnlyOnePageMessage();
			return;
		}
		openedPage = new AttachmentMessagePage(this);
		openedPage.Show(title);
	}

	public void SendMessageRequest() {
		Message message = openedPage.GetMessage();
		if (currentPageType == PageType.Sms || currentPageType == PageType.Fax) {
			// Validation goes here.
		}
	}

	private enum PageType {
		Sms,
		Mms,
		Email,
		Fax,
	}
}
