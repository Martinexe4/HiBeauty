const predict = require ('../modules/model')

exports.predit = async (req, res) => {
const image = req.file
if (!image) {
  return res.status(400).json({
    status: false,
    message: "Invalid input"
  });
}
try {
    const result = await predict.predict(image.buffer);
    return res.status(200).json({
      status: true,
      message: "Scan successful",
      result: result
    });
  } catch (err) {
    return res.status(500).json({
      status: false,
      message: "An unexpected error occurred on the server",
      err: err.toString(),
    });
  }

}