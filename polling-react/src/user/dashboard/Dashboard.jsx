import React, { useCallback, useEffect, useState } from "react";
import { getAllPolls } from "../../services/poll/Poll";
import { enqueueSnackbar } from "notistack";
import { useNavigate } from "react-router-dom";
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
  Card,
  CardHeader,
  CardContent,
  Paper,
  CardActions,
  AvatarGroup,
  Menu,
  MenuItem,
  Popover,
  IconButton,
} from "@mui/material";
import { blue } from "@mui/material/colors";
import moment from "moment/moment";
import { MoreVertOutlined, RemoveRedEyeOutlined } from "@mui/icons-material";

const Dashboard = () => {
  const [loading, setLoading] = useState(false);
  const [polls, setPolls] = useState([]);
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);
  const [selectedPoll, setSelectedPoll] = useState(null);
  const open = Boolean(anchorEl);

  const fetchData = useCallback(async () => {
    setLoading(true);

    try {
      const response = await getAllPolls();
      if (response.status === 200) {
        setPolls(response.data);
      }
    } catch (error) {
      enqueueSnackbar("Getting errors while fetching polls", {
        variant: "error",
        autoHideDuration: 5000,
      });
    } finally {
      setLoading(false);
    }
  }, [enqueueSnackbar]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handlePopoverOpen = (event, poll) => {
    setAnchorEl(event.currentTarget);
    setSelectedPoll(poll);
  };

  const handlePopoverClose = () => {
    setAnchorEl(null);
  };

  return (
    <>
      <Box sx={{ flexGrow: 1, alignItems: "center" }}>
        <Grid
          container
          spacing={3}
          direction="column"
          alignItems="center"
          sx={{ maxWidth: 600 }}
        >
          {polls.length === 0 && !loading ? (
            <Box
              sx={{
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <Typography variant="h6" color="text.secendary" gutterBottom>
                No Polls Found
              </Typography>
              <Button
                variant="contained"
                color="primary"
                onClick={() => navigate("/poll/create")}
              >
                Create a Poll
              </Button>
            </Box>
          ) : (
            polls.map((poll) => (
              <Grid item key={poll.id} xs={12} sx={{ width: "100%" }}>
                <Card sx={{ width: "100%", mt: 3 }}>
                  <CardHeader
                    avatar={
                      <Avatar sx={{ bgcolor: blue[500] }} aria-label="recipe">
                        {poll.username.charAt(0)}
                      </Avatar>
                    }
                    action={
                      <>
                        <IconButton
                          aria-label="settings"
                          onClick={(e) => handlePopoverOpen(e, poll)}
                        >
                          <MoreVertOutlined />
                        </IconButton>
                        <Popover
                          sx={{ width: "10%" }}
                          open={open && selectedPoll === poll}
                          anchorEl={anchorEl}
                          onClose={handlePopoverClose}
                          anchorOrigin={{
                            vertical: "bottom",
                            horizontal: "right",
                          }}
                        >
                          <Box>
                            <Menu
                              anchorEl={anchorEl}
                              open={open && selectedPoll === poll}
                              onClose={handlePopoverClose}
                              anchorOrigin={{
                                vertical: "bottom",
                                horizontal: "right",
                              }}
                            >
                              <MenuItem
                                onClick={() =>
                                  navigate(`/poll/${poll.id}/view`)
                                }
                              >
                                <RemoveRedEyeOutlined sx={{ mr: 1 }} />
                                View
                              </MenuItem>
                            </Menu>
                          </Box>
                        </Popover>
                      </>
                    }
                    title={poll.username}
                    subheader={moment(poll.postedDate).fromNow()}
                  />
                  <CardContent sx={{ mb: 0, pt: 0 }}>
                    <Typography
                      variant="body2"
                      color="text.primary"
                      sx={{ cursor: "pointer" }}
                      onClick={() => navigate(`/poll/${poll.id}/view`)}
                    >
                      <strong>{poll.question}</strong>
                    </Typography>
                    {poll.optionsDTOS.map((option) => (
                      <Paper
                        elevation={3}
                        sx={{ p: 1, width: "95%", mt: 1 }}
                        key={option.id}
                      >
                        {option.title}
                      </Paper>
                    ))}
                  </CardContent>

                  <CardActions
                    disableSpacing
                    sx={{
                      pt: 0,
                      justifyContent: "center",
                      textAlign: "center",
                    }}
                  >
                    <>
                      <Typography variant="body2" color="text.secondary">
                        Vote : <strong>{poll.totalVoteCount}</strong>
                      </Typography>
                      <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{ ml: 2 }}
                      >
                        Expires At:
                        <strong>
                          {moment(poll.expiryDate).format(
                            "HH:mm on MMMM D, YYYY",
                          )}
                        </strong>
                      </Typography>
                    </>
                  </CardActions>
                </Card>
              </Grid>
            ))
          )}
          ;
        </Grid>
      </Box>
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={loading}
      >
        <CircularProgress color="success" />
      </Backdrop>
    </>
  );
};

export default Dashboard;
