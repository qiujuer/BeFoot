using Microsoft.Framework.DependencyInjection;
using System;
using System.Linq;

namespace MusicBank.Models
{
    public class SampleData
    {
        public static void Initialize(IServiceProvider serviceProvider)
        {
            var context = serviceProvider.GetService<MusicContext>();
            if (context.Database.EnsureCreated())
            {
                if (!context.Artists.Any())
                {
                    var austen = context.Artists.Add(
                        new Artist { Name = "Austen", Age = 21 }).Entity;
                    var dickens = context.Artists.Add(
                        new Artist { Name = "Dickens", Age = 25 }).Entity;
                    var cervantes = context.Artists.Add(
                        new Artist { Name = "Cervantes", Age = 27 }).Entity;

                    context.Audio.AddRange(
                        new Audio()
                        {
                            Name = "Pride",
                            Type = 1,
                            Artist = austen,
                            Src = "Pride.mp3"
                        },
                        new Audio()
                        {
                            Name = "Northanger",
                            Type = 2,
                            Artist = austen,
                            Src = "Northanger.mp3"
                        },
                        new Audio()
                        {
                            Name = "David",
                            Type = 3,
                            Artist = dickens,
                            Src = "David.mp3"
                        },
                        new Audio()
                        {
                            Name = "DonQuixote",
                            Type = 1,
                            Artist = cervantes,
                            Src = "DonQuixote.mp3"
                        }
                    );
                    context.SaveChanges();
                }
            }
        }
    }
}
