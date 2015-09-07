using Microsoft.Data.Entity;

namespace MusicBank.Models
{
    public class MusicContext : DbContext
    {
        public DbSet<Audio> Audio { get; set; }
        public DbSet<Artist> Artists { get; set; }
    }
}
