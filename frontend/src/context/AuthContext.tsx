import { createContext, useContext, useState } from "react";
import type { LoginRequest, User } from "../types";
import { api, clearAuthSession, getAuthUser } from "../api/api";

interface AuthContextType {
    user : User | null;
    isAuthenticated: boolean;
    login: (credentials: LoginRequest) => Promise<void>
    logout: () => void;
}
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children}) => {
    const [user,setUser] = useState<User | null>(getAuthUser());

    const login = async (credentials: LoginRequest) => {
        const res = await api.login(credentials);
        setUser(res.user);
    };

    const logout = () => {
        api.logout();
        setUser(null);
    };

    return (
        <AuthContext.Provider value={ {user,isAuthenticated: !!user,login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}