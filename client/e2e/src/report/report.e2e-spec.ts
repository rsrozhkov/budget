import { AppPage } from './report.po';

describe('тесты report.component', () => {
  let page: AppPage;

  beforeAll(() => {
    page = new AppPage();
    page.navigateTo();
  });

  it('отображает заголовок компонента', () => {
    expect(page.getTitleText()).toEqual('Отчёт по затратам:');
  });

  it(' верно отображает форму ввода дат', () => {
    expect(page.getStartDateLabelText()).toEqual('Начало периода:');
    expect(page.getStartDateInput().isPresent()).toBeTruthy();
    expect(page.getEndDateLabelText()).toEqual('Конец периода:');
    expect(page.getEndDateInput().isPresent()).toBeTruthy();
    expect(page.getLoadButton().getText()).toEqual('Выгрузить');
    expect(page.isLoadButtonDisabled()).toBeTruthy();
  });

  it('верно отображает пустую таблицу', () => {
    expect(page.getColumnsQuantity()).toEqual(4);
    expect(page.isColumnPresent('Дата')).toBeTruthy();
    expect(page.isColumnPresent('Кто')).toBeTruthy();
    expect(page.isColumnPresent('Сумма')).toBeTruthy();
    expect(page.isColumnPresent('На что')).toBeTruthy();
    expect(page.getRowsQuantity()).toEqual(1);
  });

  //TODO тесты, которые описаны ниже работают только в хроме

  it('дата начала периода вводится', () => {
    page.getStartDateInput().click();
    page.getStartDateInput().sendKeys('01-01-2019');
    expect(page.getStartDateInputValue()).toEqual("2019-01-01");
  });

  it('дата конца периода вводится', () => {
    page.getEndDateInput().clear();
    page.getEndDateInput().sendKeys("01.02.2019");
    expect(page.getEndDateInputValue()).toEqual("2019-02-01");
  });

  it('валидация работает', () => {
    page.getStartDateInput().clear();
    page.getStartDateInput().sendKeys("01.01.2019");
    page.getEndDateInput().clear();
    page.getEndDateInput().sendKeys("01.02.2019");
    expect(page.getValidationError().isPresent()).toBeFalsy();
    expect(page.isLoadButtonDisabled()).toBeFalsy();

    page.getEndDateInput().clear();
    page.getEndDateInput().sendKeys("01.12.2018");
    expect(page.getValidationError().isPresent()).toBeTruthy();
    expect(page.isLoadButtonDisabled()).toBeTruthy();

  });

  it('отчет выгружается', () => {
    page.getStartDateInput().clear();
    page.getStartDateInput().sendKeys("01.01.2019");
    page.getEndDateInput().clear();
    page.getEndDateInput().sendKeys("18.02.2019");
    page.getLoadButton().click();
    expect(page.getRowsQuantity()).toBeGreaterThan(1);

    page.getStartDateInput().clear();
    page.getStartDateInput().sendKeys("01.01.1970");
    page.getEndDateInput().clear();
    page.getEndDateInput().sendKeys("18.02.1980");
    page.getLoadButton().click();
    expect(page.getRowsQuantity()).toEqual(1);

    page.getStartDateInput().clear();
    page.getStartDateInput().sendKeys("01.01.2019");
    page.getEndDateInput().clear();
    page.getEndDateInput().sendKeys("25.01.2019");
    page.getLoadButton().click();
    expect(page.getRowsQuantity()).toBeGreaterThan(1);
  });

});
