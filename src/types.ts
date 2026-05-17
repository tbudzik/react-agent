export type FieldType = 'string' | 'number' | 'date';

export type Field = {
  id: string;
  name: string;
  type: FieldType;
  filterable: boolean;
  minLength: number | null;
  maxLength: number | null;
};

export type ComponentItem = {
  id: string;
  name: string;
  exportCsv: boolean;
  editable: boolean;
  copyable: boolean;
  fields: Field[];
};
