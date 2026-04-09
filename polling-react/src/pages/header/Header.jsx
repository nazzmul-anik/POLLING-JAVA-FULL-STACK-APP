import React from 'react'
import {AppBar, Box, Toolbar, IconButton, Typography, Button} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import {Link} from "react-router-dom"

const Header = () => {
  return (
    <>
        <Box sx={{flexGrow: 1}}>
            <AppBar postion="static">
                <Toolbar>
                  <IconButton
                    size="large"
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    sx={{mr:2}}
                  >
                    <MenuIcon/>
                  </IconButton>

                  <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                    Polling App
                  </Typography>

                  <Button component={Link} to="/login" color="inherit">Login</Button>
                  <Button component={Link} to="/register" color="inherit">Register</Button>
                </Toolbar>
            </AppBar>
        </Box>
    </>
  )
}

export default Header