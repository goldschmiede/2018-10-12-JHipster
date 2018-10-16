import { element, by, ElementFinder } from 'protractor';

export class KategorieComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-kategorie div table .btn-danger'));
    title = element.all(by.css('jhi-kategorie div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class KategorieUpdatePage {
    pageTitle = element(by.id('jhi-kategorie-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    beschreibungInput = element(by.id('field_beschreibung'));
    titelInput = element(by.id('field_titel'));
    ueberkategorieSelect = element(by.id('field_ueberkategorie'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setBeschreibungInput(beschreibung) {
        await this.beschreibungInput.sendKeys(beschreibung);
    }

    async getBeschreibungInput() {
        return this.beschreibungInput.getAttribute('value');
    }

    async setTitelInput(titel) {
        await this.titelInput.sendKeys(titel);
    }

    async getTitelInput() {
        return this.titelInput.getAttribute('value');
    }

    async ueberkategorieSelectLastOption() {
        await this.ueberkategorieSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async ueberkategorieSelectOption(option) {
        await this.ueberkategorieSelect.sendKeys(option);
    }

    getUeberkategorieSelect(): ElementFinder {
        return this.ueberkategorieSelect;
    }

    async getUeberkategorieSelectedOption() {
        return this.ueberkategorieSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class KategorieDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-kategorie-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-kategorie'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
