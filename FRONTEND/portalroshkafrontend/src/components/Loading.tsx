import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import logo from '../assets/logo.jpg';

// Tipos
interface LoadingContextType {
  isLoading: boolean;
  showLoading: () => void;
  hideLoading: () => void;
  isTransitioning: boolean;
  setLoadingText: (text: string) => void;
}

interface LoadingProviderProps {
  children: ReactNode;
}

interface LoadingOverlayProps {
  onTransitionEnd?: () => void;
  text?: string;
}

// Contexto
const LoadingContext = createContext<LoadingContextType | null>(null);

// Hook personalizado
export const useLoading = () => {
  const context = useContext(LoadingContext);
  if (!context) throw new Error('useLoading must be used within a LoadingProvider');
  return context;
};

// Hook para usar con async
export const useAsyncLoading = () => {
  const { showLoading, hideLoading } = useLoading();

  const withLoading = async <T,>(fn: () => Promise<T>): Promise<T> => {
    try {
      showLoading();
      return await fn();
    } finally {
      hideLoading();
    }
  };

  return { withLoading };
};

// ANIMACIÓN DE FLOR - Termina exactamente en la posición del logo del login
const LoadingOverlay: React.FC<LoadingOverlayProps> = ({ onTransitionEnd, text }) => {
  const [phase, setPhase] = useState<'small' | 'growing' | 'blooming' | 'positioning'>('small');

  useEffect(() => {
    const small = setTimeout(() => setPhase('growing'), 300);
    const grow = setTimeout(() => setPhase('blooming'), 1200);
    const bloom = setTimeout(() => setPhase('positioning'), 2000);
    const done = setTimeout(() => onTransitionEnd?.(), 3000);

    return () => {
      clearTimeout(small);
      clearTimeout(grow);
      clearTimeout(bloom);
      clearTimeout(done);
    };
  }, [onTransitionEnd]);

  const getLogoStyles = () => {
    switch (phase) {
      case 'small':
        return {
          transform: 'scale(0.3)',
          transformOrigin: 'center',
          transitionDuration: '300ms',
          animationDuration: '3s',
        };
      case 'growing':
        return {
          transform: 'scale(1.2)',
          transformOrigin: 'center', 
          transitionDuration: '900ms',
          animationDuration: '2s',
        };
      case 'blooming':
        return {
          transform: 'scale(1.8)',
          transformOrigin: 'center',
          transitionDuration: '800ms',
          animationDuration: '1.5s',
        };
      case 'positioning':
        // Esta es la clave: mover el logo exactamente donde estará en el login
        // Necesita ir al centro-superior de la pantalla
        return {
          transform: 'scale(0.67) translateY(-200px)',
          transformOrigin: 'center',
          transitionDuration: '1000ms',
          animationDuration: '3s',
        };
      default:
        return {
          transform: 'scale(1)',
          transformOrigin: 'center',
          transitionDuration: '500ms',
          animationDuration: '2s',
        };
    }
  };

  return (
    <div
      className={`fixed inset-0 z-50 flex flex-col items-center justify-center transition-opacity duration-500 ${
        phase === 'positioning' ? 'opacity-0' : 'opacity-100'
      }`}
      style={{ backgroundColor: '#2E5EAA' }}
    >
      {/* Logo principal - efecto flor que termina en posición del login */}
      <div className="transition-transform ease-in-out">
        <img
          src={logo}
          alt="Logo"
          className="w-48 h-48 object-cover rounded-full shadow-lg animate-spin transition-transform ease-in-out"
          style={getLogoStyles()}
        />
      </div>

      {/* Texto opcional - solo visible en fases tempranas */}
      {text && phase !== 'positioning' && (
        <div className={`mt-8 text-center transition-opacity duration-300 ${
          phase === 'blooming' ? 'opacity-50' : 'opacity-90'
        }`}>
          <p className="text-white text-lg font-medium">
            {text}
          </p>
          <div className="flex items-center justify-center space-x-1 mt-4">
            <div className="w-2 h-2 bg-white rounded-full animate-pulse"></div>
            <div 
              className="w-2 h-2 bg-yellow-400 rounded-full animate-pulse" 
              style={{ animationDelay: '0.5s' }}
            />
            <div 
              className="w-2 h-2 bg-white rounded-full animate-pulse" 
              style={{ animationDelay: '1s' }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

// Provider
export const LoadingProvider: React.FC<LoadingProviderProps> = ({ children }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [isTransitioning, setIsTransitioning] = useState(false);
  const [loadingText, setLoadingText] = useState<string>('');

  const showLoading = () => {
    setIsLoading(true);
    setIsTransitioning(false);
  };

  const hideLoading = () => {
    setIsTransitioning(true);
    setTimeout(() => {
      setIsLoading(false);
      setIsTransitioning(false);
      setLoadingText('');
    }, 3000); // Reducido para coincidir con la nueva animación
  };

  return (
    <LoadingContext.Provider value={{ 
      isLoading, 
      showLoading, 
      hideLoading, 
      isTransitioning,
      setLoadingText 
    }}>
      {children}
      {isLoading && <LoadingOverlay text={loadingText} />}
    </LoadingContext.Provider>
  );
};

// Spinner pequeño inline (opcional)
export const InlineLoading: React.FC<{
  size?: 'sm' | 'md' | 'lg';
  className?: string;
}> = ({ size = 'md', className = '' }) => {
  const sizeClasses = {
    sm: 'w-8 h-8',
    md: 'w-12 h-12',
    lg: 'w-16 h-16',
  };

  return (
    <div className={`flex items-center justify-center ${className}`}>
      <img
        src={logo}
        alt="Logo"
        className={`${sizeClasses[size]} object-cover rounded-full animate-spin`}
        style={{ animationDuration: '1.5s' }}
      />
    </div>
  );
};

export default LoadingProvider;