export interface Ingredient {
  id?: number
  name: string
  caloriesPer100g: number
  proteinPer100g: number
  carbsPer100g: number
  fatPer100g: number
  isGlobal?: boolean
}

export interface MealIngredient {
  id: number
  ingredient: Ingredient
  quantity: number
}

export enum MealType {
  BREAKFAST = 'BREAKFAST',
  LUNCH = 'LUNCH',
  DINNER = 'DINNER',
  SNACK = 'SNACK',
  SUPPER = "SUPPER"
}

export interface Meal {
  id: number
  type: MealType
  ingredients: MealIngredient[]
  calories: number
  protein: number
  carbs: number
  fat: number
}

export interface DailyLog {
  date: string
  meals: Meal[]
  totalCalories: number
  totalProtein: number
  totalCarbs: number
  totalFat: number
}
