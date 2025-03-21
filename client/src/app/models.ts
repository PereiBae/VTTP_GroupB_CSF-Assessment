// You may use this file to create any models
export interface MenuItem {
  id: string
  name: string
  price: number
  description: string
}

export interface SelectedItem extends MenuItem {
  quantity: number
}

// Define the state interface
export interface MenuState {
  menuItems: MenuItem[];
  selectedItems: { [id: string]: number }; // id -> quantity mapping
  totalCost: number;
  totalCount: number;
  isLoading: boolean;
  error: string | null;
}
