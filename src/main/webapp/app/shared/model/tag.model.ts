import { IArtikel } from 'app/shared/model//artikel.model';

export interface ITag {
    id?: number;
    beschreibung?: string;
    titel?: string;
    artikels?: IArtikel[];
}

export class Tag implements ITag {
    constructor(public id?: number, public beschreibung?: string, public titel?: string, public artikels?: IArtikel[]) {}
}
