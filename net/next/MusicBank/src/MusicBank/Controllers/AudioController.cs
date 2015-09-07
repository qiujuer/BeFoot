using Microsoft.AspNet.Mvc;
using MusicBank.Models;
using System.Collections.Generic;
using System.Linq;

namespace MusicBank.Controllers
{
    [Route("api/[controller]")]
    public class AudioController : Controller
    {
        [FromServices]
        public MusicContext db { get; set; }

        [HttpGet]
        public IEnumerable<Audio> Get()
        {
            return db.Audio.ToList();
        }

        [HttpGet("{name}")]
        public Audio Get(string name)
        {
            Audio audio = db.Audio.Where(a => (a.Name == name)).FirstOrDefault();
            return audio;
        }
    }
}
