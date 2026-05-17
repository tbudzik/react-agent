import { ComponentItem } from '../types';

const API_URL = 'http://localhost:8080/api/components';

export async function listComponents(): Promise<ComponentItem[]> {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error('Failed to fetch components:', error);
    return [];
  }
}

export async function getComponent(id: string): Promise<ComponentItem | null> {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) return null;
    return await response.json();
  } catch (error) {
    console.error(`Failed to fetch component ${id}:`, error);
    return null;
  }
}

export async function createComponent(
  payload: Omit<ComponentItem, 'id' | 'fields'>
): Promise<ComponentItem> {
  try {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error('Failed to create component:', error);
    throw error;
  }
}

export async function updateComponent(
  id: string,
  patch: Partial<Omit<ComponentItem, 'id' | 'fields'>>
): Promise<ComponentItem | null> {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(patch)
    });
    if (!response.ok) return null;
    return await response.json();
  } catch (error) {
    console.error(`Failed to update component ${id}:`, error);
    return null;
  }
}

export async function deleteComponent(id: string): Promise<void> {
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
  } catch (error) {
    console.error(`Failed to delete component ${id}:`, error);
  }
}

export async function generateComponentFile(id: string): Promise<Blob | null> {
  try {
    const response = await fetch(`http://localhost:8080/generate?componentId=${encodeURIComponent(id)}`);
    if (!response.ok) return null;
    return await response.blob();
  } catch (error) {
    console.error(`Failed to generate file for component ${id}:`, error);
    return null;
  }
}

export async function updateComponentFields(
  id: string,
  fields: ComponentItem['fields']
): Promise<ComponentItem | null> {
  // This operation is implicit in the backend via field CRUD endpoints
  // Just return the component as-is for now
  return getComponent(id);
}
