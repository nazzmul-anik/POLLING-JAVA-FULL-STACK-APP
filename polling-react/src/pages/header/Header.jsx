import React, { useEffect, useState } from "react";
import {
  AppBar,
  Box,
  Toolbar,
  IconButton,
  Typography,
  Button,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { isTokenValid, removeToken } from "../../utility/Common";

const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isUserLoggedIn, setIsUserLoggedIn] = useState(false);

  const handleSignOut = () => {
    navigate("/login");
    removeToken();
  };

  useEffect(() => {
    const isLoggedIn = isTokenValid();
    setIsUserLoggedIn(isLoggedIn);
  }, [location]);

  useEffect(() => {
    const interval = setInterval(() => {
      if (!isTokenValid()) {
        setIsUserLoggedIn(false);
        handleSignOut();
      }
    }, 1800000);

    return () => clearInterval(interval);
  }, []);

  return (
    <>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar postion="static">
          <Toolbar>
            <IconButton
              size="large"
              edge="start"
              color="inherit"
              aria-label="menu"
              sx={{ mr: 2 }}
            >
              <MenuIcon />
            </IconButton>

            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              Polling App
            </Typography>

            {isUserLoggedIn ? (
              <>
                <Button component={Link} to="/dashboard" color="inherit">
                  Dashboard
                </Button>
                <Button component={Link} to="/poll" color="inherit">
                  Post Poll
                </Button>
                <Button component={Link} to="/my-polls" color="inherit">
                  My Polls
                </Button>
                <Button onClick={handleSignOut} color="inherit">
                  Logout
                </Button>
              </>
            ) : (
              <>
                <Button component={Link} to="/login" color="inherit">
                  Login
                </Button>
                <Button component={Link} to="/register" color="inherit">
                  Register
                </Button>
              </>
            )}
          </Toolbar>
        </AppBar>
      </Box>
    </>
  );
};

export default Header;
