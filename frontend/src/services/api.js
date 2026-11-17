
const API_BASE_URL = "http://localhost:8080";

export async function apiGet(endpoint) {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`);

    if (!response.ok) {
      throw new Error(`API Error: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("FaultLens API Error:", error);
    throw error;
  }
}
