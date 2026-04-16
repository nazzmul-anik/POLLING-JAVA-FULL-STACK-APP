import axiosInstance from "../../environment/AxiosInstance";

export const postPoll = async (pollDTO) => {
  const response = await axiosInstance.post("/api/user/poll", pollDTO);
  return response;
};

export const getAllPolls = async () => {
  const response = await axiosInstance.get("/api/user/polls");
  return response;
};

export const getMyPolls = async () => {
  const response = await axiosInstance.get("/api/user/my-polls");
  return response;
};

export const deletePollById = async (id) => {
  const response = await axiosInstance.delete(`/api/user/poll/${id}`);
  return response;
};

export const getPollById = async (id) => {
  const response = await axiosInstance.get(`/api/user/poll/${id}`);
  return response;
};

export const giveLikeToPoll = async (pollId) => {
  const response = await axiosInstance.get(`/api/user/poll/like/${pollId}`);
  return response;
};

export const postCommentOnPoll = async (commentDTO) => {
  const response = await axiosInstance.post(
    "/api/user/poll/comment",
    commentDTO,
  );
  return response;
};

export const postVoteOnPoll = async (voteDTO) => {
  const response = await axiosInstance.post("/api/user/poll/vote", voteDTO);
  return response;
};
