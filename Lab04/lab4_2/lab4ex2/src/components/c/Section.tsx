import { useContext } from 'react';
import { LevelContext } from '../../context/c/LevelContext';

import { ReactNode } from 'react';

interface SectionProps {
  children: ReactNode;
  isFancy: boolean;
}

export default function Section({ children, isFancy }: SectionProps) {
  const level = useContext(LevelContext);
  return (
    <section className={
      'section ' +
      (isFancy ? 'fancy' : '')
    }>
      <LevelContext.Provider value={level + 1}>
        {children}
      </LevelContext.Provider>
    </section>
  );
}
