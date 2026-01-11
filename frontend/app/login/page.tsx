import LoginForm from "@/components/auth/LoginForm";
import { AuthCard } from "@/components/auth/AuthCard";
import Layout from "@/components/auth/Layout";

export default function LoginPage() {
  return (
    
      <Layout>
        <AuthCard>
            <LoginForm />
        </AuthCard>
      </Layout>

  );
}
