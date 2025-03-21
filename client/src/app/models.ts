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
}

export interface OrderItem extends MenuItem {
  quantity: number;
  subtotal?: number
}

export interface ConfirmOrder {
  username: string
  password: string
  items: {
    id: string;
    price: number;
    quantity: number;
  }[];
}

export interface Receipt{
  orderId: string;
  paymentId: string;
  total: number;
  timestamp:number
}
