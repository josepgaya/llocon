export class RestapiField {
    name: string;
    type: string;
    required: boolean;
    minLength: number;
    maxLength: number;
    disabledForCreate: boolean;
    disabledForUpdate: boolean;
    hiddenInGrid: boolean;
    hiddenInForm: boolean;
    hiddenInLov: boolean;
    enumValues: string[];
    lovResource: string;
    lovDetailFieldName: string;
    lovWithDetailInput: boolean;
    width?: number;
}
