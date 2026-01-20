"use client"

import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { IngredientList } from "./IngredientList"
import { AddIngredientForm } from "./AddIngredientForm"
import { Meal } from "@/types/meals"

interface AddMealModalProps {
  open: boolean
  onClose: () => void,
    meal: Meal,
    date: Date
}

export function AddMealModal({ open, onClose,date, meal }: AddMealModalProps) {
  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="flex h-[75vh] max-w-xl flex-col border-white/10 bg-black/90">
        <DialogHeader>
          <DialogTitle className="text-emerald-400">
            Dodaj potrawę / składnik
          </DialogTitle>
        </DialogHeader>

        <Tabs defaultValue="all" className="flex flex-1 flex-col">
          <TabsList className="grid w-full grid-cols-3 rounded-lg bg-white/5 p-1">
            <TabsTrigger
              value="all"
              className="w-full rounded-lg text-zinc-400 cursor-pointer data-[state=active]:bg-emerald-600 data-[state=active]:text-black data-[state=active]:cursor-default">
              Baza
            </TabsTrigger>
            <TabsTrigger
              value="mine"
             className="w-full rounded-lg text-zinc-400 cursor-pointer data-[state=active]:bg-emerald-600 data-[state=active]:text-black data-[state=active]:cursor-default">
              Moje
            </TabsTrigger>
            <TabsTrigger
              value="new"
            className="w-full rounded-lg text-zinc-400 cursor-pointer data-[state=active]:bg-emerald-600 data-[state=active]:text-black data-[state=active]:cursor-default">
              Dodaj
            </TabsTrigger>
          </TabsList>

          <TabsContent value="all" className="mt-4 flex-1 overflow-y-auto">
            <IngredientList type="all" />
          </TabsContent>

          <TabsContent value="mine" className="mt-4 flex-1 overflow-y-auto">
            <IngredientList type="mine" />
          </TabsContent>

          <TabsContent value="new" className="mt-4 flex-1 overflow-y-auto">
            <AddIngredientForm meal={meal} date={date}/>
          </TabsContent>
        </Tabs>
      </DialogContent>
    </Dialog>
  )
}
