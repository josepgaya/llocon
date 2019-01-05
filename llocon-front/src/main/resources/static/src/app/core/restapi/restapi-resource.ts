import { RestapiField } from './restapi-field';
import { RestapiGrid } from './restapi-grid';

export class RestapiResource {
    apiUrl: string;
    descriptionField: string;
    fields: RestapiField[];
    grids: RestapiGrid[];
}
