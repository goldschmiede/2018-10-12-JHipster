import { element, by, ElementFinder } from 'protractor';

export class ArtikelComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-artikel div table .btn-danger'));
    title = element.all(by.css('jhi-artikel div h2#page-heading span')).first();

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

export class ArtikelUpdatePage {
    pageTitle = element(by.id('jhi-artikel-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    artikelnummerInput = element(by.id('field_artikelnummer'));
    beschreibungInput = element(by.id('field_beschreibung'));
    titelInput = element(by.id('field_titel'));
    preisInput = element(by.id('field_preis'));
    eingestelltAmInput = element(by.id('field_eingestelltAm'));
    verfuegbarkeitSelect = element(by.id('field_verfuegbarkeit'));
    kategorieSelect = element(by.id('field_kategorie'));
    tagsSelect = element(by.id('field_tags'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setArtikelnummerInput(artikelnummer) {
        await this.artikelnummerInput.sendKeys(artikelnummer);
    }

    async getArtikelnummerInput() {
        return this.artikelnummerInput.getAttribute('value');
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

    async setPreisInput(preis) {
        await this.preisInput.sendKeys(preis);
    }

    async getPreisInput() {
        return this.preisInput.getAttribute('value');
    }

    async setEingestelltAmInput(eingestelltAm) {
        await this.eingestelltAmInput.sendKeys(eingestelltAm);
    }

    async getEingestelltAmInput() {
        return this.eingestelltAmInput.getAttribute('value');
    }

    async setVerfuegbarkeitSelect(verfuegbarkeit) {
        await this.verfuegbarkeitSelect.sendKeys(verfuegbarkeit);
    }

    async getVerfuegbarkeitSelect() {
        return this.verfuegbarkeitSelect.element(by.css('option:checked')).getText();
    }

    async verfuegbarkeitSelectLastOption() {
        await this.verfuegbarkeitSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async kategorieSelectLastOption() {
        await this.kategorieSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async kategorieSelectOption(option) {
        await this.kategorieSelect.sendKeys(option);
    }

    getKategorieSelect(): ElementFinder {
        return this.kategorieSelect;
    }

    async getKategorieSelectedOption() {
        return this.kategorieSelect.element(by.css('option:checked')).getText();
    }

    async tagsSelectLastOption() {
        await this.tagsSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async tagsSelectOption(option) {
        await this.tagsSelect.sendKeys(option);
    }

    getTagsSelect(): ElementFinder {
        return this.tagsSelect;
    }

    async getTagsSelectedOption() {
        return this.tagsSelect.element(by.css('option:checked')).getText();
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

export class ArtikelDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-artikel-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-artikel'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
