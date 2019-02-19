import { MembersPage } from './members.po';
import {protractor} from "protractor/built/ptor";

describe('тесты members.component', () => {
  let page: MembersPage;
  let existName: string;
  let nonExistName: string;

  beforeAll(() => {
    page = new MembersPage();
    page.navigateTo();
    existName = 'Василий Чапаев';
    nonExistName = 'Васька';
  });

  it('отображает заголовок компонента', () => {
    expect(page.getTitleText()).toEqual('Члены семьи:');
  });

  it('форма добавления отображается верно', () => {
    expect(page.getAddMemberHeaderText()).toEqual('Добавить нового члена семьи:');
    expect(page.getNameLabelText()).toEqual('Имя:');
    expect(page.getNameInput().isPresent()).toBeTruthy();
    expect(page.getNameInputValue()).toEqual('');
    expect(page.getAddButton().isPresent()).toBeTruthy();
    expect(page.isAddButtonDisabled()).toBeTruthy();
  });

  it('имя вводится в форму', () => {
    page.setName(nonExistName);
    expect(page.getNameInputValue()).toEqual(nonExistName);
  });

  it('перечень отображается верно', () => {
    expect(page.getMembersListHeaderText()).toEqual('Перечень всех членов семьи:');
    expect(page.getMembersListLength()).toBeGreaterThan(0);
  });

  it('имя существующего члена есть в перечне', () => {
    expect(page.isListContainsName(existName)).toBeTruthy();
  });

  it('валидация вводда имени работает', () => {
    page.getNameInput().clear();
    page.setName(nonExistName);
    expect(page.isAddButtonDisabled()).toBeFalsy();

    page.getNameInput().clear();
    page.setName('a');
    page.getNameInput().sendKeys(protractor.Key.BACK_SPACE);
    expect(page.getNameRequiredError()).toEqual('Нужно ввести имя.');
    expect(page.isAddButtonDisabled()).toBeTruthy();

    page.getNameInput().clear();
    page.setName('a');
    expect(page.getNameLengthError()).toEqual('Имя должно содержть 2-30 символов.');
    expect(page.isAddButtonDisabled()).toBeTruthy();

    page.getNameInput().clear();
    page.setNameOverMaxLen(30);
    expect(page.getNameLen()).toBe(30);
    expect(page.isAddButtonDisabled()).toBeFalsy();

    page.getNameInput().clear();
    page.setName('ава+ф-авуу.ю');
    expect(page.getNameFormatError()).toEqual('Недопустимый символ.');
    expect(page.isAddButtonDisabled()).toBeTruthy();

    page.getNameInput().clear();
    page.setName(existName);
    expect(page.getNameExistError()).toEqual('Имя уже занято, введите другое.');
    expect(page.isAddButtonDisabled()).toBeTruthy();

  });

  it('новый член семьи добавляется и удаляется', () => {
    expect(page.isListContainsName(nonExistName)).toBeFalsy();
    page.getNameInput().clear();
    page.setName(nonExistName);
    page.getAddButton().click();
    expect(page.isListContainsName(nonExistName)).toBeTruthy();

    page.getDeleteButtonByName(nonExistName).click();
    expect(page.isListContainsName(nonExistName)).toBeFalsy();
  });

  it('член семьи с транзакциями не удаляется', () => {
    page.getDeleteButtonByName(existName).click();
    expect(page.getAlertText()).toContain('Удаление не возможно');
    expect(page.isListContainsName(existName)).toBeTruthy();
  });

  it('верно переходит на форму редактирования члена семьи', () => {
    page.getListElementByName(existName).click();
    let expectedText = "Редактирование " + existName;
    expect(page.getMemberEditText()).toEqual(expectedText);
  });

});
