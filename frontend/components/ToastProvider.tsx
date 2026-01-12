'use client'

import { createContext, useContext, useState, useCallback } from 'react'
import { AnimatePresence } from 'framer-motion'
import { ToastProps } from '@/types/toast'
import { v4 as uuidv4 } from 'uuid'
import { Toast } from './ui/toast'
// Definiujemy typ Toast jako rozszerzenie ToastProps (bo potrzebujemy id)
type Toast = ToastProps

// Typ wejściowy do tworzenia toastów (bez id)
type ToastInput = Omit<Toast, 'id'>

interface ToastContextType {
  toast:    (props: ToastInput) => void
  success:  (props: Omit<ToastInput, 'variant'>) => void
  error:    (props: Omit<ToastInput, 'variant'>) => void
  warning:  (props: Omit<ToastInput, 'variant'>) => void
  info:     (props: Omit<ToastInput, 'variant'>) => void
  dismiss:  (id: string) => void
}

const ToastContext = createContext<ToastContextType | undefined>(undefined)

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [toasts, setToasts] = useState<Toast[]>([])

  const addToast = useCallback((toastInput: ToastInput) => {
    const id = uuidv4()
    const newToast: Toast = { ...toastInput, id }
    setToasts(prev => [...prev, newToast])
  }, [])

  const removeToast = useCallback((id: string) => {
    setToasts(prev => prev.filter(t => t.id !== id))
  }, [])

  const toast    = useCallback((props: ToastInput) => addToast(props), [addToast])
  const success  = useCallback((props: Omit<ToastInput, 'variant'>) => 
    addToast({ ...props, variant: 'success' }), [addToast])
  
  const error    = useCallback((props: Omit<ToastInput, 'variant'>) => 
    addToast({ ...props, variant: 'error' }), [addToast])
  
  const warning  = useCallback((props: Omit<ToastInput, 'variant'>) => 
    addToast({ ...props, variant: 'warning' }), [addToast])
  
  const info     = useCallback((props: Omit<ToastInput, 'variant'>) => 
    addToast({ ...props, variant: 'info' }), [addToast])

  const contextValue: ToastContextType = {
    toast,
    success,
    error,
    warning,
    info,
    dismiss: removeToast,
  }

  return (
    <ToastContext.Provider value={contextValue}>
      {children}
      <div className="pointer-events-none fixed bottom-6 right-6 z-[100] flex max-h-screen w-full flex-col-reverse items-end gap-3 sm:bottom-8 sm:right-8">
        <AnimatePresence mode="popLayout">
          {toasts.map(toast => (
            <Toast
              key={toast.id}
              toast={toast}
              onRemove={() => removeToast(toast.id)}
            />
          ))}
        </AnimatePresence>
      </div>
    </ToastContext.Provider>
  )
}

export function useToast() {
  const context = useContext(ToastContext)
  if (!context) {
    throw new Error('useToast musi być używany wewnątrz ToastProvider')
  }
  return context
}