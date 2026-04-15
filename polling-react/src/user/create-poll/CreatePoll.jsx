import { Email, HowToVote, HowToVoteRounded } from "@mui/icons-material";
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
  Chip,
  Autocomplete,
} from "@mui/material";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { enqueueSnackbar, useSnackbar } from "notistack";
import { DateTimePicker } from "@mui/x-date-pickers";
import { postPoll } from "../../services/poll/Poll";
import { saveToken } from "../../utility/Common";

const defaultTheme = createTheme();
const CreatePoll = () => {
  const [formData, setFormData] = useState({
    question: "",
    options: [],
    expiryDate: null,
  });
  const [loading, setLoading] = useState(false);
  // const { equeueSnackbar } = useSnackbar();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const obj = {
        question: formData.question,
        options: formData.options,
        expiryDate: formData.expiryDate,
      };
      const response = await postPoll(obj);
      if (response.status === 201) {
        navigate("/dashboard");
        enqueueSnackbar(`Post a Poll Successfully !!`, {
          variant: "success",
          autoHideDuration: 5000,
        });
      }
    } catch (error) {
      enqueueSnackbar(`Getting error while creating poll: ${error}`, {
        variant: "error",
        autoHideDuration: 5000,
      });
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
              <HowToVoteRounded />
            </Avatar>
            <Typography component="h1" variant="h5">
              Create Poll
            </Typography>
            <Box
              component="form"
              onSubmit={handleSubmit}
              noValidate
              sx={{ mt: 1 }}
            >
              <TextField
                id="outlined-multiline-static"
                label="Enter Question"
                multiline
                rows={2}
                required
                sx={{ width: "70ch" }}
                autoFocus
                margin="normal"
                fullWidth
                name="question"
                value={formData.question}
                onChange={(e) =>
                  setFormData({ ...formData, question: e.target.value })
                }
              />

              <Autocomplete
                multiple
                sx={{ width: "70ch" }}
                options={[]}
                freeSolo
                value={formData.options}
                onChange={(event, newValue) =>
                  setFormData({ ...formData, options: newValue })
                }
                renderTags={(value, getTagProps) => {
                  value.map((options, index) => (
                    <Chip
                      key={index}
                      label={options}
                      {...getTagProps({ index })}
                    />
                  ));
                }}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    margin="normal"
                    label="Options"
                    name="options"
                  />
                )}
              ></Autocomplete>

              <DateTimePicker
                sx={{ mt: 3, width: "70ch" }}
                label="Expiration Date"
                value={formData.expiryDate}
                onChange={(date) =>
                  setFormData({ ...formData, expiryDate: date })
                }
              />

              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                disabled={
                  !formData.question ||
                  !formData.options ||
                  !formData.expiryDate
                }
              >
                {loading ? (
                  <CircularProgress color="success" size={24} />
                ) : (
                  "Post Poll"
                )}
              </Button>
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

export default CreatePoll;
