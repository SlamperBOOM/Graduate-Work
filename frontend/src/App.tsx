import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { appRoutes } from './hooks/useRoute';
import { MainPage } from './pages/MainPage';
import { ErrorPage } from './pages/ErrorPage';
import { NewPrediction } from './pages/predictions/NewPrediction';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index path={appRoutes.mainPage} element={<MainPage/>}/>
        <Route path={appRoutes.prediction} element={<NewPrediction/>}/>
        <Route path='*' element={<ErrorPage/>}/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
