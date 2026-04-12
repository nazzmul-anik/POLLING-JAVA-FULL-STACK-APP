import { useState } from "react";
import "./App.css";
import Header from "./pages/header/Header";
import { Route, Routes } from "react-router-dom";
import Signup from "./pages/auth/signup/Signup";
import Login from "./pages/auth/login/Login";
import Dashboard from "./user/dashboard/Dashboard";
import CreatePoll from "./user/create-poll/CreatePoll";
import ViewMyPoll from "./user/view-my-poll/ViewMyPoll";
import ViewPollDetails from "./user/view-poll-details/ViewPollDetails";

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <Header />
      <Routes>
        <Route path="/register" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/poll/create" element={<CreatePoll />} />
        <Route path="/poll/my-poll" element={<ViewMyPoll />} />
        <Route path="/poll/:id/:view" element={<ViewPollDetails />} />
      </Routes>
    </>
  );
}

export default App;
