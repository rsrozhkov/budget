import { MemberEditPage } from './member-edit.po';
import {protractor} from "protractor/built/ptor";

describe('тесты member-edit.component', () => {
  let page: MemberEditPage;
  let memberId;
  let memberName;
  let existName;
  let nonExistName;

  beforeAll(() => {
    memberId = 1;
    memberName = 'Василий Чапаев';
    existName = 'Пётр Исаев';
    nonExistName = 'Новое Имя';
    page = new MemberEditPage();
    page.navigateTo();
    page.navigateToId(memberId);
  });

  it('отображает заголовок компонента', () => {
    expect(page.getTitleText()).toEqual('Редактирование '+memberName);
  });

  it('верно отображает номер члена семьи', () => {
    expect(page.getIdLabelText()).toEqual('№: ' + memberId);
  });

  it('отображает метку поля воода', () => {
    expect(page.getNameLabelText()).toEqual('Новое имя:');
  });

  it('отображает поле воода имени', () => {
    expect(page.getNameInput().isPresent()).toBeTruthy();
    expect(page.getNameInputValue()).toEqual(memberName);
  });

  it('верно отображает кнопки', () => {
    expect(page.getGoBackButton().getText()).toEqual('Отмена');
    expect(page.getSaveButton().getText()).toEqual('ОК');
    expect(page.isSaveButtonDisabled()).toBeFalsy();
  });

  it('поле ввода имени редактируется', () => {
    page.getNameInput().clear();
    page.setName(nonExistName);
    expect(page.getNameInputValue()).toEqual(nonExistName);
  });

  it('валидация вводда имени работает', () => {
    page.getNameInput().clear();
    page.setName('a');
    page.getNameInput().sendKeys(protractor.Key.BACK_SPACE);
    expect(page.getNameRequiredError()).toEqual('Нужно ввести имя.');
    expect(page.isSaveButtonDisabled()).toBeTruthy();

    page.getNameInput().clear();
    page.setName('a');
    expect(page.getNameLengthError()).toEqual('Имя должно содержть 2-30 символов.');
    expect(page.isSaveButtonDisabled()).toBeTruthy();

    page.getNameInput().clear();
    page.setNameOverMaxLen(30);
    expect(page.getNameLen()).toBe(30);
    expect(page.isSaveButtonDisabled()).toBeFalsy();

    page.getNameInput().clear();
    page.setName('ава+ф-авуу.ю');
    expect(page.getNameFormatError()).toEqual('Недопустимый символ.');
    expect(page.isSaveButtonDisabled()).toBeTruthy();

    page.getNameInput().clear();
    page.setName(existName);
    expect(page.getNameExistError()).toEqual('Это имя уже занято, введите другое.');
    expect(page.isSaveButtonDisabled()).toBeTruthy();

  });

  it('кнопка "отмена" работает верно', () => {
    page.getGoBackButton().click();
    expect(page.getMembersTitleText()).toEqual('Члены семьи:');
    page.navigateToId(memberId);
  });

  it('возврат если имя не изменилось', () => {
    page.getNameInput().clear();
    page.setName(memberName);
    page.getSaveButton().click();
    expect(page.getMembersTitleText()).toEqual('Члены семьи:');
    expect(page.getListElementByName(memberName).isPresent()).toBeTruthy();
    page.navigateToId(memberId);
  });

  it('имя члена семьи изменяется', () => {
    page.getNameInput().clear();
    page.setName(nonExistName);
    page.getSaveButton().click();
    page.navigateTo();
    expect(page.getMembersTitleText()).toEqual('Члены семьи:');
    expect(page.getListElementByName(nonExistName).isPresent()).toBeTruthy();
    expect(page.getListElementByName(memberName).isPresent()).toBeFalsy();
    page.navigateToId(memberId);

    page.getNameInput().clear();
    page.setName(memberName);
    page.getSaveButton().click();
    page.navigateTo();
    expect(page.getMembersTitleText()).toEqual('Члены семьи:');
    expect(page.getListElementByName(nonExistName).isPresent()).toBeFalsy();
    expect(page.getListElementByName(memberName).isPresent()).toBeTruthy();
    page.navigateToId(memberId);
  });

});
