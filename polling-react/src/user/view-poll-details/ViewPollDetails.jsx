import React, { useState, useCallback, useEffect } from "react";
import {
  getPollById,
  giveLikeToPoll,
  postCommentOnPoll,
  postVoteOnPoll,
} from "../../services/poll/Poll";
import { enqueueSnackbar } from "notistack";
import { Navigate, useNavigate, useParams } from "react-router-dom";
import {
  Avatar,
  Backdrop,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  CircularProgress,
  Container,
  Divider,
  Grid,
  IconButton,
  LinearProgress,
  Menu,
  MenuItem,
  Paper,
  Popover,
  TextField,
  Typography,
} from "@mui/material";
import {
  AddCommentOutlined,
  CheckCircleOutlineOutlined,
  FavoriteBorderOutlined,
  FavoriteOutlined,
  MoreVertOutlined,
  RemoveRedEyeOutlined,
} from "@mui/icons-material";
import moment from "moment";
import { blue } from "@mui/material/colors";

const ViewPollDetails = () => {
  const [loading, setLoading] = useState(false);
  const [poll, setPoll] = useState();
  const [likeCount, setLikeCount] = useState();
  const [commentsCount, setCommentsCount] = useState();
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const { id } = useParams();
  const navigate = useNavigate();

  const fetchData = useCallback(async () => {
    setLoading(true);

    try {
      const response = await getPollById(id);
      if (response.status === 200) {
        setPoll(response.data.pollDTO);
        setLikeCount(response.data.likeCount);
        setCommentsCount(response.data.commentCount);
        setComments(response.data.commentDTOList);
      }
    } catch (error) {
      enqueueSnackbar("Getting errors while fetching poll", {
        variant: "error",
        autoHideDuration: 5000,
      });
    } finally {
      setLoading(false);
    }
  }, [id, enqueueSnackbar]);

  const handleAddVote = async (pollId, optionId) => {
    try {
      const obj = {
        optionId: optionId,
        pollId: pollId,
      };
      // console.log(obj);

      const response = await postVoteOnPoll(obj);
      if (response.status === 200) {
        enqueueSnackbar(`Poll voted successfully !!`, {
          variant: "success",
          autoHideDuration: 5000,
        });
        fetchData();
      }
    } catch (error) {
      if (error.response && error.response.status === 406) {
        enqueueSnackbar("Poll has expired and cannot be voted on.", {
          variant: "error",
          autoHideDuration: 5000,
        });
        fetchData();
      } else {
        enqueueSnackbar("Error while posting vote.", {
          variant: "error",
          autoHideDuration: 5000,
        });
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLikeClick = async (pollId) => {
    setLoading(true);
    try {
      const response = await giveLikeToPoll(pollId);
      if (response.status === 200) {
        fetchData();
      }
    } catch (error) {
      enqueueSnackbar("Error while giving like.", {
        variant: "error",
        autoHideDuration: 5000,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCommentSubmit = async (pollId) => {
    setLoading(true);
    try {
      const obj = {
        pollId: pollId,
        content: newComment,
      };
      const response = await postCommentOnPoll(obj);
      if (response.status === 200) {
        enqueueSnackbar("Comment posted successfully!!", {
          variant: "success",
          autoHideDuration: 5000,
        });
        fetchData();
      }
    } catch (error) {
      enqueueSnackbar("Error while posting comment", {
        variant: "error",
        autoHideDuration: 5000,
      });
    } finally {
      setNewComment("");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <>
      <Container sx={{ mt: 10 }}>
        <Box sx={{ flexGrow: 1, alignItems: "center" }}>
          <Grid
            container
            spacing={3}
            direction="column"
            alignItems="center"
            sx={{ maxWidth: 600, mx: "auto", width: "100%" }}
          >
            {poll === undefined && !loading ? (
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
                <Typography variant="h4" sx={{ fontSize: "3rem" }}>
                  🙁
                </Typography>
                <Typography>Oops!! Poll Not Found.</Typography>
              </Box>
            ) : (
              poll && (
                <Grid item key={poll.id} xs={12} sx={{ width: "100%" }}>
                  <Card sx={{ width: "100%", mt: 3 }}>
                    <CardHeader
                      avatar={
                        <Avatar sx={{ bgcolor: blue[500] }} aria-label="recipe">
                          {poll.username.charAt(0)}
                        </Avatar>
                      }
                      title={poll.username}
                      subheader={moment(poll.postedDate).fromNow()}
                    />
                    <CardContent sx={{ mb: 0, pt: 0 }}>
                      <Typography
                        variant="body2"
                        color="text.primary"
                        sx={{ cursor: "pointer" }}
                      >
                        <strong>{poll.question}</strong>
                      </Typography>
                      {poll.voted || poll.isExpired
                        ? poll.optionsDTOS.map((option) => (
                            <React.Fragment key={option.id}>
                              <div
                                style={{ position: "relative", width: "100%" }}
                              >
                                <LinearProgress
                                  variant="determinate"
                                  value={
                                    isNaN(
                                      (option.voteCount / poll.totalVoteCount) *
                                        100,
                                    )
                                      ? 0
                                      : (option.voteCount /
                                          poll.totalVoteCount) *
                                        100
                                  }
                                  sx={{ height: 30, bgcolor: "#CCD7DF", mt: 1 }}
                                />
                                <div
                                  style={{
                                    position: "absolute",
                                    top: "50%",
                                    left: 0,
                                    width: "100%",
                                    transform: "translateY(-50%)",
                                    display: "flex",
                                    alignItems: "center",
                                  }}
                                >
                                  <Typography>
                                    {option.title} -{" "}
                                    {isNaN(
                                      (option.voteCount / poll.totalVoteCount) *
                                        100,
                                    )
                                      ? "0%"
                                      : `${(option.voteCount / poll.totalVoteCount) * 100}%`}
                                  </Typography>
                                  {option.userVotedThisOption && (
                                    <CheckCircleOutlineOutlined
                                      sx={{
                                        marginLeft: "4px",
                                        fontSize: "20px",
                                      }}
                                    />
                                  )}
                                </div>
                              </div>
                            </React.Fragment>
                          ))
                        : poll.optionsDTOS.map((option) => (
                            <Paper
                              elevation={3}
                              sx={{ p: 1, width: "95%", mt: 1 }}
                              key={option.id}
                              onClick={() => handleAddVote(poll.id, option.id)}
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
                      {poll.isExpired ? (
                        <Typography variant="body2" color="text.secondary">
                          <strong>{poll.totalVoteCount}</strong> votes . Final
                          results
                        </Typography>
                      ) : (
                        <>
                          <Typography variant="body2" color="text.secondary">
                            Vote: <strong>{poll.totalVoteCount}</strong>
                          </Typography>
                          <Typography
                            variant="body2"
                            color="text.secondary"
                            sx={{ ml: 2 }}
                          >
                            Expires At :{" "}
                            <strong>
                              {moment(poll.expiryDate).format(
                                "HH:mm on MMM D, YYYY",
                              )}
                            </strong>
                          </Typography>
                        </>
                      )}
                    </CardActions>
                    <Divider />
                    <Box sx={{ width: "100%", p: 1, cursor: "pointer" }}>
                      <Grid
                        container
                        rowSpacing={1}
                        columnSpacing={{ xs: 1, sm: 2, md: 2 }}
                      >
                        <Grid
                          item
                          xs={6}
                          sx={{ justifyContent: "center", textAlign: "center" }}
                        >
                          <Grid
                            item
                            xs={6}
                            sx={{
                              justifyContent: "center",
                              textAlign: "center",
                            }}
                          >
                            <Grid
                              container
                              direction="row"
                              alignItems="center"
                              justifyContent="center"
                            >
                              {poll.isLiked ? (
                                <FavoriteOutlined sx={{ color: "red" }} />
                              ) : (
                                <FavoriteBorderOutlined
                                  onClick={() => handleLikeClick(poll.id)}
                                />
                              )}
                              <Typography sx={{ ml: 1 }}>
                                {likeCount}
                              </Typography>
                            </Grid>
                          </Grid>
                          <Grid
                            container
                            direction="row"
                            alignItems="center"
                            justifyContent="center"
                          >
                            <AddCommentOutlined sx={{ color: blue[500] }} />
                            <Typography sx={{ ml: 1 }}>
                              {commentsCount}
                            </Typography>
                          </Grid>
                        </Grid>
                      </Grid>
                    </Box>
                  </Card>

                  <Box mt={3}>
                    <Card sx={{ width: "100%", mt: 2, borderRadius: 2 }}>
                      <CardContent>
                        <Typography
                          variant="h6"
                          gutterBottom
                          sx={{
                            fontWeight: "bold",
                            mb: 1,
                          }}
                        >
                          Comments
                        </Typography>

                        <Box
                          sx={{ maxHeight: "200px", overflowY: "auto", p: 1 }}
                        >
                          {comments.map((comment, index) => (
                            <>
                              <Divider />
                              <Typography
                                key={index}
                                variant="body1"
                                gutterBottom
                                sx={{
                                  display: "flex",
                                  alignItems: "flex-start",
                                  mb: 2,
                                  p: 1,
                                  borderRadius: 2,
                                  bgcolor: "#f5f5f5",
                                }}
                              >
                                <strong>{comment.content}</strong>
                              </Typography>
                              <p>
                                Posted {moment(comment.commentDate).fromNow()}{" "}
                                by {comment.username}
                              </p>
                              <Divider />
                            </>
                          ))}
                        </Box>

                        <Box sx={{ display: "flex", gap: 1, mt: 2 }}>
                          <TextField
                            fullWidth
                            size="small"
                            placeholder="Write a comment..."
                            value={newComment}
                            onChange={(e) => setNewComment(e.target.value)}
                          />

                          <Button
                            variant="contained"
                            disabled={!newComment.trim()}
                            onClick={() => handleCommentSubmit(poll.id)}
                          >
                            Post
                          </Button>
                        </Box>
                      </CardContent>
                    </Card>
                  </Box>
                </Grid>
              )
            )}
          </Grid>
        </Box>
      </Container>

      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={loading}
      >
        <CircularProgress color="success" />
      </Backdrop>
    </>
  );
};

export default ViewPollDetails;
