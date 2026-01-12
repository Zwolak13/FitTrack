export type ToastVariant = 'success' | 'error' | 'warning' | 'info' | 'default'

export interface ToastProps {
  id: string
  title?: string
  description?: string
  variant?: ToastVariant
  duration?: number
  action?: {
    label: string
    onClick: () => void
  }
}