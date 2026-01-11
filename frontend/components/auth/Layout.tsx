export default function Layout({ children }: { children: React.ReactNode }){

    return(
    <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-[#070b16]">
          <div className="absolute inset-0 bg-gradient-to-br from-emerald-500/10 via-cyan-500/5 to-blue-600/10" />
    
          <div className="absolute -top-32 -left-32 h-[420px] w-[420px] rounded-full bg-emerald-500/25 blur-[120px]" />
          <div className="absolute bottom-0 -right-32 h-[420px] w-[420px] rounded-full bg-cyan-500/25 blur-[120px]" />
    
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(255,255,255,0.04),transparent_70%)]" />

          {children}
    </div>
    )
}