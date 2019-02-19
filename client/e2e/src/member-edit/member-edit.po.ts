import {browser, by, element} from 'protractor';
import {MembersPage} from "../members/members.po";

export class MemberEditPage extends MembersPage{

  navigateToId(id) {
    return browser.get('/members/'+id);
  }

  getTitleText() {
    return element(by.css('app-member-edit h3')).getText();
  }

  getIdLabelText() {
    return element(by.id('idLabel')).getText();
  }

  getGoBackButton() {
    return element(by.id('goBackButton'));
  }

  getSaveButton() {
    return element(by.id('saveButton'));
  }

  isSaveButtonDisabled() {
    return this.getSaveButton().getAttribute('disabled');
  }

  getMembersTitleText() {
    return element(by.css('app-members h3')).getText();
  }
}
