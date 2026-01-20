"use client"

import { useState } from "react"
import { Plus, X } from "lucide-react"
import { useFetchIngredients } from "@/hooks/dashboard/ingredients/useFetchIngredients"
import { AddExistingIngredientPayload, useAddExistingIngredient } from "@/hooks/dashboard/ingredients/useAddExistingIngredient"
import { useDeleteIngredient } from "@/hooks/dashboard/ingredients/useDeleteIngredient"

interface Ingredient {
  id: number
  name: string
  calories: number
  protein: number
  carbs: number
  fat: number
}

interface IngredientListProps {
  type: "all" | "mine"
  mealId: number
  date: string
}

export function IngredientList({ type, mealId, date }: IngredientListProps) {
  const [inputValue, setInputValue] = useState("")
  const [query, setQuery] = useState("")
  const [quantities, setQuantities] = useState<Record<number, number>>({})

  const { data: ingredients = [], isLoading, isError } = useFetchIngredients(type, query)
  const addMutation = useAddExistingIngredient()

  const handleAdd = (i: Ingredient) => {
    const payload: AddExistingIngredientPayload = {
      mealId,
      ingredientId: i.id,
      quantity: type === "all" ? quantities[i.id] ?? 1 : 1,
      date,
    }
    addMutation.mutate(payload)
  }

  return (
    <div className="flex flex-col gap-3 h-full">
      <input
        value={inputValue}
        onChange={(e) => setInputValue(e.target.value)}
        onKeyDown={(e) => { if (e.key === "Enter") setQuery(inputValue) }}
        placeholder="Szukaj składnika..."
        className="rounded-lg border border-white/10 bg-black px-3 py-2 text-sm text-white outline-none focus:border-emerald-500"
      />

      {isLoading && <p className="text-zinc-400">Ładowanie…</p>}
      {isError && <p className="text-red-500">Błąd podczas ładowania składników</p>}
      {!isLoading && ingredients.length === 0 && <p className="text-zinc-500 w-full text-center">Brak składników</p>}

      <div className="space-y-2 overflow-y-auto flex-1">
        {ingredients.map((i) => {
          const quantity = type === "all" ? quantities[i.id] ?? 1 : 1

          return (
            <div
              key={i.id}
              className="flex flex-col md:flex-row md:items-center justify-between rounded-lg border border-white/10 px-3 py-2 text-sm text-zinc-300 hover:border-emerald-500/40 bg-black/20"
            >
              <div className="flex flex-col gap-1">
                <span className="font-semibold">{i.name}</span>
                <div className="flex gap-2 text-xs md:text-sm">
                  <span className="text-emerald-400">{i.caloriesPer100g * quantity} kcal</span>
                  <span className="text-orange-500">{i.carbsPer100g * quantity}gW</span>
                  <span className="text-white">{i.proteinPer100g * quantity}gB</span>
                  <span className="text-yellow-400">{i.fatPer100g * quantity}gT</span>
                </div>
              </div>

              <div className="flex gap-2 mt-2 md:mt-0">
                {type === "all" && (
                  <input
                    type="number"
                    min={1}
                    value={quantities[i.id] ?? 1}
                    onChange={(e) =>
                      setQuantities((prev) => ({ ...prev, [i.id]: Number(e.target.value) }))
                    }
                    className="w-16 rounded bg-black/50 px-2 py-1 text-sm text-white"
                  />
                )}
                <button
                  className="bg-emerald-600 px-2 py-1 rounded hover:bg-emerald-500 flex items-center justify-center"
                  // @ts-expect-error dont want to make it wierd
                  onClick={() => handleAdd(i)}
                >
                  <Plus size={16} />
                </button>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}
