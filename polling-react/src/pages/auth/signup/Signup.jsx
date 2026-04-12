import { Email } from "@mui/icons-material";
import React, { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import {
  Avatar,
  Backdrop,
  Typography,
  Container,
  TextField,
  Button,
  CircularProgress,
  Grid,
  CssBaseline,
  Box,
  Link,
} from "@mui/material";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { useSnackbar } from "notistack";
import { signup } from "../Auth";
import { saveToken } from "../../../utility/Common";

const defaultTheme = createTheme();

const Signup = () => {
  const { enqueueSnackbar } = useSnackbar();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleInputChange = async (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    console.log("Inside submit");

    e.preventDefault();
    setLoading(true);

    try {
      const response = await signup(formData);

      if (response.status == 201) {
        const responseData = response.data;
        saveToken(responseData.jwtToken);
        navigate("/dashboard");
        enqueueSnackbar(`Welcome ${responseData.name}`, {
          variant: "success",
          autoHideDuration: 5000,
        });
      }
    } catch (error) {
      if (error.response && error.response.status == 409) {
        enqueueSnackbar("User already exists", {
          variant: "error",
          autoHideDuration: 5000,
        });
      } else {
        enqueueSnackbar("Sign up failed!", {
          variant: "error",
          autoHideDuration: 5000,
        });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <ThemeProvider theme={defaultTheme}>
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Box
            sx={{
              marginTop: 8,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Avatar sr={{ m: 1, bgcolor: "primary.main" }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign Up
            </Typography>
            <Box
              component="form"
              onSubmit={handleSubmit}
              noValidate
              sx={{ mt: 3 }}
            >
              <Grid container spacing={2}>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    margin="normal"
                    autoComplete="given-name"
                    name="firstName"
                    required
                    fullWidth
                    id="firtName"
                    label="First Name"
                    autoFocus
                    value={formData.firstName}
                    onChange={handleInputChange}
                  />
                </Grid>

                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    margin="normal"
                    autoComplete="family-name"
                    name="lastName"
                    required
                    fullWidth
                    id="firtName"
                    label="Last Name"
                    autoFocus
                    value={formData.lastName}
                    onChange={handleInputChange}
                  />
                </Grid>

                <Grid size={{ xs: 12 }}>
                  <TextField
                    margin="normal"
                    autoComplete="email"
                    name="email"
                    required
                    fullWidth
                    id="email"
                    label="Email Address"
                    autoFocus
                    value={formData.email}
                    onChange={handleInputChange}
                  />
                </Grid>

                <Grid size={{ xs: 12 }}>
                  <TextField
                    margin="normal"
                    autoComplete="new-password"
                    name="password"
                    required
                    fullWidth
                    id="password"
                    label="Password"
                    autoFocus
                    value={formData.password}
                    onChange={handleInputChange}
                  />
                </Grid>

                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{ mt: 3, mb: 2 }}
                  disabled={
                    !formData.email ||
                    !formData.password ||
                    !formData.lastName ||
                    !formData.firstName
                  }
                >
                  {loading ? (
                    <CircularProgress color="success" size={24} />
                  ) : (
                    "Sign Up"
                  )}
                </Button>

                <Grid container justifyContent="flex-end">
                  <Grid>
                    <Link
                      style={{ cursor: "pointer" }}
                      variant="body2"
                      onClick={() => navigate("/login")}
                    >
                      {"Already have an account? Sign In"}
                    </Link>
                  </Grid>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Container>
      </ThemeProvider>
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={loading}
      >
        <CircularProgress color="success" />
      </Backdrop>
    </>
  );
};

export default Signup;
