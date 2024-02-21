import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { appRoutes } from './hooks/useRoute';
import { MainPage } from './pages/MainPage';
import { ErrorPage } from './pages/ErrorPage';
import { NewPredictionPage } from './pages/predictions/NewPredictionPage';
import { ResultsPage } from './pages/predictions/ResultsPage';
import { TaxDataPage } from './pages/taxes/TaxDataPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index path={appRoutes.mainPage} element={<MainPage/>}/>
        <Route path={appRoutes.prediction} element={<NewPredictionPage/>}/>
        <Route path={appRoutes.results} element={<ResultsPage/>}/>
        <Route path={appRoutes.taxes} element={<TaxDataPage/>}/>
        <Route path='*' element={<ErrorPage/>}/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
