import { useMemo, useState, useEffect } from 'react';
import { FieldType, Field, ComponentItem } from './types';
import {
  listComponents,
  createComponent,
  updateComponent,
  deleteComponent,
  getComponent,
  generateComponentFile
} from './services/componentsApi';
import { createField, updateField, deleteField } from './services/fieldsApi';

const newComponentTemplate: Omit<ComponentItem, 'id' | 'fields'> = {
  name: '',
  exportCsv: false,
  editable: true,
  copyable: false
};

const newFieldTemplate: Omit<Field, 'id'> = {
  name: '',
  type: 'string',
  filterable: false,
  minLength: null,
  maxLength: null
};

function formatFieldType(type: FieldType) {
  switch (type) {
    case 'string':
      return 'String';
    case 'number':
      return 'Liczba';
    case 'date':
      return 'Data';
  }
}

function App() {
  const [components, setComponents] = useState<ComponentItem[]>([]);
  const [selectedComponentId, setSelectedComponentId] = useState<string>('');
  const [componentForm, setComponentForm] = useState<Omit<ComponentItem, 'id' | 'fields'>>(newComponentTemplate);
  const [editingComponentId, setEditingComponentId] = useState<string | null>(null);
  const [fieldForm, setFieldForm] = useState<Omit<Field, 'id'>>(newFieldTemplate);
  const [editingFieldId, setEditingFieldId] = useState<string | null>(null);

  const selectedComponent = useMemo(
    () => components.find((item) => item.id === selectedComponentId) ?? null,
    [components, selectedComponentId]
  );

  useEffect(() => {
    const load = async () => {
      const data = await listComponents();
      setComponents(data);
      if (data.length > 0 && !selectedComponentId) setSelectedComponentId(data[0].id);
    };
    load();
  }, []);

  const resetComponentForm = () => {
    setComponentForm(newComponentTemplate);
    setEditingComponentId(null);
  };

  const resetFieldForm = () => {
    setFieldForm(newFieldTemplate);
    setEditingFieldId(null);
  };

  const handleSelectComponent = (id: string) => {
    setSelectedComponentId(id);
    resetFieldForm();
  };

  const handleComponentSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    (async () => {
      if (editingComponentId) {
        await updateComponent(editingComponentId, componentForm);
        const data = await listComponents();
        setComponents(data);
        resetComponentForm();
        return;
      }

      const created = await createComponent(componentForm);
      const data = await listComponents();
      setComponents(data);
      setSelectedComponentId(created.id);
      resetComponentForm();
    })();
  };

  const handleComponentEdit = (item: ComponentItem) => {
    setComponentForm({
      name: item.name,
      exportCsv: item.exportCsv,
      editable: item.editable,
      copyable: item.copyable
    });
    setEditingComponentId(item.id);
    setSelectedComponentId(item.id);
  };

  const handleComponentDelete = (id: string) => {
    (async () => {
      await deleteComponent(id);
      const data = await listComponents();
      setComponents(data);
      if (selectedComponentId === id) {
        setSelectedComponentId(data.length ? data[0].id : '');
      }
    })();
  };

  const handleFieldSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!selectedComponent) return;
    (async () => {
      if (editingFieldId) {
        await updateField(selectedComponent.id, editingFieldId, fieldForm);
        const data = await listComponents();
        setComponents(data);
        resetFieldForm();
        return;
      }

      await createField(selectedComponent.id, fieldForm);
      const data = await listComponents();
      setComponents(data);
      resetFieldForm();
    })();
  };

  const handleFieldEdit = (field: Field) => {
    setFieldForm({
      name: field.name,
      type: field.type,
      filterable: field.filterable,
      minLength: field.minLength,
      maxLength: field.maxLength
    });
    setEditingFieldId(field.id);
  };

  const handleFieldDelete = (fieldId: string) => {
    if (!selectedComponent) return;
    (async () => {
      await deleteField(selectedComponent.id, fieldId);
      const data = await listComponents();
      setComponents(data);
    })();
  };

  const handleDownloadComponent = async () => {
    if (!selectedComponent) return;
    const blob = await generateComponentFile(selectedComponent.id);
    if (!blob) return;

    const fileName = `${selectedComponent.name.replace(/[^a-zA-Z0-9_\- ]/g, '_')}.txt`;
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(url);
  };

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white/90 backdrop-blur-sm">
        <div className="mx-auto flex max-w-7xl flex-col gap-4 px-4 py-6 sm:px-6 lg:px-8 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm uppercase tracking-[0.25em] text-sky-600">Panel CRUD</p>
            <h1 className="text-3xl font-semibold">Zarządzanie komponentami i polami</h1>
          </div>
          <div className="max-w-xl text-sm text-slate-600">
            Aplikacja pozwala dodawać, edytować i usuwać komponenty oraz pola przypisane do wybranego komponentu.
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="grid gap-6 xl:grid-cols-[360px_minmax(0,1fr)]">
          <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
            <div className="mb-4 flex items-center justify-between gap-4">
              <div>
                <h2 className="text-xl font-semibold">Komponenty</h2>
                <p className="text-sm text-slate-500">Wybierz komponent aby zarządzać jego polami.</p>
              </div>
              <button
                type="button"
                className="rounded-full bg-sky-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-sky-700"
                onClick={() => {
                  resetComponentForm();
                  setSelectedComponentId('');
                }}
              >
                Nowy
              </button>
            </div>
            <div className="space-y-3">
              {components.map((item) => (
                <button
                  key={item.id}
                  type="button"
                  onClick={() => handleSelectComponent(item.id)}
                  className={`w-full rounded-2xl border p-4 text-left transition ${
                    selectedComponentId === item.id ? 'border-sky-400 bg-sky-50' : 'border-slate-200 bg-white hover:border-slate-300'
                  }`}
                >
                  <div className="flex items-center justify-between gap-3">
                    <div>
                      <p className="font-semibold">{item.name}</p>
                      <p className="text-xs text-slate-500">{item.fields.length} pole(i)</p>
                    </div>
                    <span className="rounded-full bg-slate-100 px-2 py-1 text-xs text-slate-700">
                      {item.exportCsv ? 'CSV' : 'Brak CSV'}
                    </span>
                  </div>
                </button>
              ))}
            </div>

            <div className="mt-6 rounded-3xl bg-slate-50 p-5">
              <h3 className="mb-4 text-lg font-semibold">Formularz Komponentu</h3>
              <form onSubmit={handleComponentSubmit} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-700">Nazwa</label>
                  <input
                    type="text"
                    value={componentForm.name}
                    onChange={(event) => setComponentForm({ ...componentForm, name: event.target.value })}
                    required
                    className="mt-2 w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                  />
                </div>
                <div className="grid gap-3 sm:grid-cols-2">
                  <label className="flex items-center gap-3 rounded-2xl border border-slate-200 bg-white px-4 py-3">
                    <input
                      type="checkbox"
                      checked={componentForm.exportCsv}
                      onChange={(event) => setComponentForm({ ...componentForm, exportCsv: event.target.checked })}
                      className="h-4 w-4 rounded border-slate-300 text-sky-600"
                    />
                    <span className="text-sm text-slate-700">Eksport do CSV</span>
                  </label>
                  <label className="flex items-center gap-3 rounded-2xl border border-slate-200 bg-white px-4 py-3">
                    <input
                      type="checkbox"
                      checked={componentForm.editable}
                      onChange={(event) => setComponentForm({ ...componentForm, editable: event.target.checked })}
                      className="h-4 w-4 rounded border-slate-300 text-sky-600"
                    />
                    <span className="text-sm text-slate-700">Możliwość edycji</span>
                  </label>
                  <label className="flex items-center gap-3 rounded-2xl border border-slate-200 bg-white px-4 py-3 sm:col-span-2">
                    <input
                      type="checkbox"
                      checked={componentForm.copyable}
                      onChange={(event) => setComponentForm({ ...componentForm, copyable: event.target.checked })}
                      className="h-4 w-4 rounded border-slate-300 text-sky-600"
                    />
                    <span className="text-sm text-slate-700">Możliwość kopiowania elementów</span>
                  </label>
                </div>
                <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
                  <button
                    type="submit"
                    className="inline-flex items-center justify-center rounded-2xl bg-sky-600 px-4 py-3 text-sm font-semibold text-white transition hover:bg-sky-700"
                  >
                    {editingComponentId ? 'Zapisz zmianę' : 'Dodaj komponent'}
                  </button>
                  {editingComponentId && (
                    <button
                      type="button"
                      onClick={resetComponentForm}
                      className="inline-flex items-center justify-center rounded-2xl border border-slate-300 bg-white px-4 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-100"
                    >
                      Anuluj
                    </button>
                  )}
                </div>
              </form>
            </div>
          </section>

          <section className="grid gap-6">
            <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
              <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
                <div>
                  <h2 className="text-xl font-semibold">Szczegóły komponentu</h2>
                  <p className="text-sm text-slate-500">
                    Wybierz komponent po lewej, aby edytować jego właściwości i pola.
                  </p>
                </div>
                {selectedComponent && (
                  <div className="flex flex-wrap gap-2">
                    <button
                      type="button"
                      onClick={() => selectedComponent && handleComponentEdit(selectedComponent)}
                      className="rounded-2xl bg-slate-100 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-200"
                    >
                      Edytuj komponent
                    </button>
                    <button
                      type="button"
                      onClick={handleDownloadComponent}
                      className="rounded-2xl bg-sky-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-sky-700"
                    >
                      Pobierz
                    </button>
                    <button
                      type="button"
                      onClick={() => handleComponentDelete(selectedComponent.id)}
                      className="rounded-2xl bg-rose-500 px-4 py-2 text-sm font-semibold text-white transition hover:bg-rose-600"
                    >
                      Usuń komponent
                    </button>
                  </div>
                )}
              </div>

              {selectedComponent ? (
                <div className="mt-6 grid gap-4 sm:grid-cols-2">
                  <div className="rounded-3xl border border-slate-200 bg-slate-50 p-4">
                    <p className="text-sm font-medium text-slate-600">Nazwa</p>
                    <p className="mt-2 text-lg font-semibold text-slate-900">{selectedComponent.name}</p>
                  </div>
                  <div className="rounded-3xl border border-slate-200 bg-slate-50 p-4">
                    <p className="text-sm font-medium text-slate-600">Opcje</p>
                    <div className="mt-2 space-y-2 text-sm text-slate-700">
                      <p>CSV: {selectedComponent.exportCsv ? 'Tak' : 'Nie'}</p>
                      <p>Edycja: {selectedComponent.editable ? 'Tak' : 'Nie'}</p>
                      <p>Kopiowanie: {selectedComponent.copyable ? 'Tak' : 'Nie'}</p>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="mt-6 rounded-3xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-slate-500">
                  Wybierz komponent lub dodaj nowy, aby zobaczyć jego właściwości.
                </div>
              )}
            </div>

            <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
              <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
                <div>
                  <h2 className="text-xl font-semibold">Pola komponentu</h2>
                  <p className="text-sm text-slate-500">Lista pól przypisanych do wybranego komponentu.</p>
                </div>
                <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold uppercase tracking-[0.16em] text-slate-600">
                  {selectedComponent ? selectedComponent.fields.length : 0} pole(i)
                </span>
              </div>

              {selectedComponent ? (
                <div className="mt-6 space-y-4">
                  {selectedComponent.fields.length ? (
                    selectedComponent.fields.map((field) => (
                      <div key={field.id} className="rounded-3xl border border-slate-200 bg-slate-50 p-4">
                        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                          <div>
                            <p className="font-semibold text-slate-900">{field.name}</p>
                            <p className="text-sm text-slate-500">Typ: {formatFieldType(field.type)}</p>
                          </div>
                          <div className="flex flex-wrap gap-2">
                            <button
                              type="button"
                              onClick={() => handleFieldEdit(field)}
                              className="rounded-2xl bg-slate-100 px-3 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-200"
                            >
                              Edytuj
                            </button>
                            <button
                              type="button"
                              onClick={() => handleFieldDelete(field.id)}
                              className="rounded-2xl bg-rose-500 px-3 py-2 text-sm font-semibold text-white transition hover:bg-rose-600"
                            >
                              Usuń
                            </button>
                          </div>
                        </div>
                        <div className="mt-3 grid gap-2 sm:grid-cols-3">
                          <p className="text-sm text-slate-600">Filtr: {field.filterable ? 'Tak' : 'Nie'}</p>
                          <p className="text-sm text-slate-600">Min dł.: {field.minLength ?? '—'}</p>
                          <p className="text-sm text-slate-600">Max dł.: {field.maxLength ?? '—'}</p>
                        </div>
                      </div>
                    ))
                  ) : (
                    <div className="rounded-3xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-slate-500">
                      Brak pól. Dodaj pierwsze pole używając formularza poniżej.
                    </div>
                  )}

                  <div className="rounded-3xl bg-slate-50 p-5">
                    <h3 className="mb-4 text-lg font-semibold">Formularz pola</h3>
                    <form onSubmit={handleFieldSubmit} className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-slate-700">Nazwa pola</label>
                        <input
                          type="text"
                          value={fieldForm.name}
                          onChange={(event) => setFieldForm({ ...fieldForm, name: event.target.value })}
                          required
                          className="mt-2 w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                        />
                      </div>
                      <div className="grid gap-4 sm:grid-cols-2">
                        <div>
                          <label className="block text-sm font-medium text-slate-700">Typ</label>
                          <select
                            value={fieldForm.type}
                            onChange={(event) => setFieldForm({ ...fieldForm, type: event.target.value as FieldType })}
                            className="mt-2 w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                          >
                            <option value="string">String</option>
                            <option value="number">Liczba</option>
                            <option value="date">Data</option>
                          </select>
                        </div>
                        <label className="flex items-center gap-3 rounded-2xl border border-slate-200 bg-white px-4 py-3">
                          <input
                            type="checkbox"
                            checked={fieldForm.filterable}
                            onChange={(event) => setFieldForm({ ...fieldForm, filterable: event.target.checked })}
                            className="h-4 w-4 rounded border-slate-300 text-sky-600"
                          />
                          <span className="text-sm text-slate-700">Dostępne na filtrach</span>
                        </label>
                      </div>
                      <div className="grid gap-4 sm:grid-cols-2">
                        <div>
                          <label className="block text-sm font-medium text-slate-700">Minimalna długość</label>
                          <input
                            type="number"
                            min={0}
                            value={fieldForm.minLength ?? ''}
                            onChange={(event) =>
                              setFieldForm({
                                ...fieldForm,
                                minLength: event.target.value ? Number(event.target.value) : null
                              })
                            }
                            className="mt-2 w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                          />
                        </div>
                        <div>
                          <label className="block text-sm font-medium text-slate-700">Maksymalna długość</label>
                          <input
                            type="number"
                            min={0}
                            value={fieldForm.maxLength ?? ''}
                            onChange={(event) =>
                              setFieldForm({
                                ...fieldForm,
                                maxLength: event.target.value ? Number(event.target.value) : null
                              })
                            }
                            className="mt-2 w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                          />
                        </div>
                      </div>
                      <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
                        <button
                          type="submit"
                          className="inline-flex items-center justify-center rounded-2xl bg-sky-600 px-4 py-3 text-sm font-semibold text-white transition hover:bg-sky-700"
                        >
                          {editingFieldId ? 'Zapisz pole' : 'Dodaj pole'}
                        </button>
                        {editingFieldId && (
                          <button
                            type="button"
                            onClick={resetFieldForm}
                            className="inline-flex items-center justify-center rounded-2xl border border-slate-300 bg-white px-4 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-100"
                          >
                            Anuluj
                          </button>
                        )}
                      </div>
                    </form>
                  </div>
                </div>
              ) : (
                <div className="mt-6 rounded-3xl border border-dashed border-slate-300 bg-slate-50 p-8 text-center text-slate-500">
                  Wybierz komponent, aby dodać lub zmienić pola.
                </div>
              )}
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}

export default App;
