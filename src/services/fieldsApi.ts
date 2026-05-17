import { Field } from '../types';

const API_URL = 'http://localhost:8080/api/components';

export async function listFields(componentId: string): Promise<Field[] | null> {
  try {
    const response = await fetch(`${API_URL}/${componentId}/fields`);
    if (!response.ok) return null;
    return await response.json();
  } catch (error) {
    console.error(`Failed to fetch fields for component ${componentId}:`, error);
    return null;
  }
}

export async function createField(componentId: string, payload: Omit<Field, 'id'>): Promise<Field | null> {
  try {
    const response = await fetch(`${API_URL}/${componentId}/fields`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    if (!response.ok) return null;
    return await response.json();
  } catch (error) {
    console.error(`Failed to create field for component ${componentId}:`, error);
    return null;
  }
}

export async function updateField(
  componentId: string,
  fieldId: string,
  patch: Partial<Omit<Field, 'id'>>
): Promise<Field | null> {
  try {
    const response = await fetch(`${API_URL}/${componentId}/fields/${fieldId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(patch)
    });
    if (!response.ok) return null;
    return await response.json();
  } catch (error) {
    console.error(`Failed to update field ${fieldId}:`, error);
    return null;
  }
}

export async function deleteField(componentId: string, fieldId: string): Promise<boolean> {
  try {
    const response = await fetch(`${API_URL}/${componentId}/fields/${fieldId}`, { method: 'DELETE' });
    if (!response.ok) return false;
    return true;
  } catch (error) {
    console.error(`Failed to delete field ${fieldId}:`, error);
    return false;
  }
}
