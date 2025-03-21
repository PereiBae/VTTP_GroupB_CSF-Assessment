import {inject, Injectable} from '@angular/core';
import {ComponentStore} from '@ngrx/component-store';
import {MenuItem, MenuState} from './models';
import {RestaurantService} from './restaurant.service';

// Initial state
const INITIAL_STATE: MenuState = {
  menuItems: [],
  selectedItems: {},
  totalCost: 0,
  totalCount: 0,
};

@Injectable({
  providedIn: 'root'
})
export class MenuStoreService extends ComponentStore<MenuState>{

  private restaurantService = inject(RestaurantService)

  constructor() {
    // Initialize the store, initially the store is empty
    super(INITIAL_STATE);
  }

  // SELECTORS
  readonly menuItems$ = this.select(state => state.menuItems);
  readonly selectedItems$ = this.select(state => state.selectedItems);
  readonly totalCost$ = this.select(state => state.totalCost);
  readonly totalCount$ = this.select(state => state.totalCount);

  // Combined selector to check if an item is selected
  readonly getQuantity = (id: string) => this.select(
    state => state.selectedItems[id] || 0
  );

  // UPDATERS (reducers)
  readonly setMenuItems = this.updater(
    (state, menuItems: MenuItem[]) => ({
      ...state,
      menuItems
    })
  );


  readonly addItem = this.updater(
    (state, itemId: string) => {
      const currentQuantity = state.selectedItems[itemId] || 0;
      const newSelectedItems = {
        ...state.selectedItems,
        [itemId]: currentQuantity + 1
      };

      return {
        ...state,
        selectedItems: newSelectedItems,
        ...this.calculateTotals(state.menuItems, newSelectedItems)
      };
    }
  );

  readonly removeItem = this.updater(
    (state, itemId: string) => {
      const currentQuantity = state.selectedItems[itemId] || 0;

      if (currentQuantity <= 0) {
        return state;
      }

      const newSelectedItems = { ...state.selectedItems };

      if (currentQuantity === 1) {
        delete newSelectedItems[itemId];
      } else {
        newSelectedItems[itemId] = currentQuantity - 1;
      }

      return {
        ...state,
        selectedItems: newSelectedItems,
        ...this.calculateTotals(state.menuItems, newSelectedItems)
      };
    }
  );

  loadMenuItems(): void {
    this.restaurantService.getMenuItems().subscribe({
      next: (menuItems) => this.setMenuItems(menuItems),
    });
  }

  // Get selected items with quantities for order placement
  readonly getSelectedItemsWithQuantities = this.select(
    this.menuItems$,
    this.selectedItems$,
    (menuItems, selectedItems) => {
      return Object.entries(selectedItems).map(([id, quantity]) => {
        const item = menuItems.find(item => item.id === id);
        return {
          ...item,
          quantity
        };
      }).filter(item => item && item.quantity > 0);
    }
  );

  // Helper method to calculate totals
  private calculateTotals(menuItems: MenuItem[], selectedItems: { [id: string]: number }) {
    let totalCost = 0;
    let totalCount = 0;

    Object.entries(selectedItems).forEach(([id, quantity]) => {
      const item = menuItems.find(item => item.id === id);
      if (item) {
        totalCost += item.price * quantity;
        totalCount += quantity;
      }
    });

    return { totalCost, totalCount };
  }

}
