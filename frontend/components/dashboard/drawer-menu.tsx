'use client'

import { motion } from 'framer-motion'
import { Button } from '@/components/ui/button'

interface DrawerMenuProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function DrawerMenu({ open, onOpenChange }: DrawerMenuProps) {
  if (!open) return null

  return (
    <>
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="fixed inset-0 bg-black/60 backdrop-blur-sm z-40"
        onClick={() => onOpenChange(false)}
      />
      <motion.div
        initial={{ x: '100%' }}
        animate={{ x: 0 }}
        exit={{ x: '100%' }}
        transition={{ type: 'spring', damping: 28, stiffness: 300 }}
        className="fixed right-0 top-0 z-50 h-full w-80 bg-gradient-to-b from-zinc-950 to-black border-l border-white/10 backdrop-blur-xl shadow-2xl"
      >
        <div className="p-6">
          <div className="mb-8">
            <h2 className="text-2xl font-bold bg-gradient-to-r from-emerald-400 to-cyan-400 bg-clip-text text-transparent">
              FitTrack
            </h2>
          </div>

          <nav className="space-y-2">
            {['Profil', 'Historia', 'Statystyki', 'Ustawienia', 'Wyloguj się'].map((item) => (
              <Button
                key={item}
                variant="ghost"
                className="w-full justify-start text-zinc-300 hover:text-white hover:bg-white/5"
              >
                {item}
              </Button>
            ))}
          </nav>
        </div>
      </motion.div>
    </>
  )
}